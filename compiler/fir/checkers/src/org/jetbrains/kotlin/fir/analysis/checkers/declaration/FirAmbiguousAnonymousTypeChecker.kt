/*
 * Copyright 2010-2021 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.fir.analysis.checkers.declaration

import org.jetbrains.kotlin.descriptors.Visibilities
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.analysis.diagnostics.DiagnosticReporter
import org.jetbrains.kotlin.fir.analysis.diagnostics.FirErrors
import org.jetbrains.kotlin.fir.analysis.diagnostics.reportOn
import org.jetbrains.kotlin.fir.declarations.*
import org.jetbrains.kotlin.fir.declarations.utils.isInline
import org.jetbrains.kotlin.fir.declarations.utils.visibility
import org.jetbrains.kotlin.fir.symbols.impl.FirAnonymousObjectSymbol
import org.jetbrains.kotlin.fir.types.coneType
import org.jetbrains.kotlin.fir.types.toSymbol

object FirAmbiguousAnonymousTypeChecker : FirBasicDeclarationChecker() {
    override fun check(declaration: FirDeclaration, context: CheckerContext, reporter: DiagnosticReporter) {
        if (declaration !is FirSimpleFunction && declaration !is FirProperty) return
        require(declaration is FirCallableDeclaration)
        if (context.containingDeclarations.any { it.isLocalMember || it is FirAnonymousObject }) return

        val containingCallableVisibility = declaration.visibility
        val isInlineFunction = declaration.isInline

        // Skip if the current declaration is private or package private. But do check if the declaration is private inline as per KT-33917.
        if (containingCallableVisibility != Visibilities.Public &&
            containingCallableVisibility != Visibilities.Protected &&
            containingCallableVisibility != Visibilities.Internal &&
            !(containingCallableVisibility == Visibilities.Private && isInlineFunction)
        ) {
            return
        }

        val classSymbol = declaration.returnTypeRef.coneType.toSymbol(context.session) ?: return
        if (classSymbol is FirAnonymousObjectSymbol) {
            // Any anonymous object that has only one super type is already approximated to the super type by
            // org.jetbrains.kotlin.fir.types.TypeUtilsKt#hideLocalTypeIfNeeded. Hence, any remaining anonymous object
            reporter.reportOn(
                declaration.source,
                FirErrors.AMBIGUOUS_ANONYMOUS_TYPE_INFERRED,
                classSymbol.resolvedSuperTypeRefs.map { it.coneType },
                context
            )
        }
    }
}