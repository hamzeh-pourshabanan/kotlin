/*
 * Copyright 2010-2021 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.idea.test.framework

import com.intellij.openapi.util.io.FileUtil
import org.jetbrains.kotlin.idea.test.KotlinLightCodeInsightFixtureTestCase
import java.io.File

abstract class AbstractKtIdeaTest : KotlinLightCodeInsightFixtureTestCase() {
    protected open val allowedDirectives: List<TestFileDirective<*>> = emptyList()

    protected fun doTest(path: String) {
        val testDataFile = File(path)
        val text = FileUtil.loadFile(testDataFile)
        val testFileStructure = createTestFileStructure(text, testDataFile)
        doTestByFileStructure(testFileStructure)
    }

    private fun createTestFileStructure(text: String, testDataFile: File): TestFileStructure {
        val files = FileSplitter.splitIntoFiles(text, testDataFile.name)
        val mainFile = createTestFile(files.first(), isMainFile = true)
        val testFiles = files.drop(1).map { file ->
            createTestFile(file, isMainFile = false)
        }
        val directives = parseDirectives(text)
        return TestFileStructure(
            filePath = testDataFile.toPath(),
            caretPosition = getCaretPosition(text),
            directives = directives,
            mainFile = mainFile as TestFile.KtTestFile,
            otherFiles = testFiles
        )
    }

    private fun getCaretPosition(text: String) = text.indexOfOrNull(KtTest.CARET_SYMBOL)

    private fun parseDirectives(text: String): TestFileDirectives {
        val directives = text.lineSequence().mapNotNull(::extractDirectiveIfAny)
        return TestFileDirectives(directives.toMap())
    }

    private fun extractDirectiveIfAny(line: String): Pair<String, Any>? {
        val directive = allowedDirectives.firstOrNull { directive -> line.startsWith(directive.name) } ?: return null
        val value = line.substringAfter(directive.name).trim()
        val parsedValue = directive.parse(value)
            ?: error("Invalid ${directive.name} value `$value`")
        return directive.name to parsedValue
    }

    private fun createTestFile(file: FileSplitter.FileNameWithText, isMainFile: Boolean): TestFile {
        val psiFile = if (isMainFile) {
            myFixture.configureByText(file.name, file.text)
        } else {
            myFixture.addFileToProject(file.name, file.text)
        }
        return TestFile.createByPsiFile(psiFile)
    }

    abstract fun doTestByFileStructure(fileStructure: TestFileStructure)
}

private object FileSplitter {
    data class FileNameWithText(val name: String, val text: String)

    fun splitIntoFiles(text: String, defaultName: String): List<FileNameWithText> {
        val result = mutableListOf<FileNameWithText>()
        val stopAt = text.indexOfOrNull(KtTest.RESULT_DIRECTIVE) ?: text.length
        var index = text.indexOfOrNull(KtTest.FILE_DIRECTIVE) ?: return listOf(FileNameWithText(defaultName, text))
        while (index < stopAt) {
            val eolIndex = text.indexOfOrNull("\n", index) ?: text.length
            val fileName = text.substring(index + KtTest.FILE_DIRECTIVE.length, eolIndex).trim()
            val nextFileIndex = text.indexOfOrNull(KtTest.FILE_DIRECTIVE, index + 1) ?: stopAt
            val fileText = text.substring(eolIndex, nextFileIndex).trim()
            index = nextFileIndex
            result += FileNameWithText(fileName, fileText)
        }
        return result
    }
}

private fun String.indexOfOrNull(substring: String) =
    indexOf(substring).takeIf { it >= 0 }

private fun String.indexOfOrNull(substring: String, startingIndex: Int) =
    indexOf(substring, startingIndex).takeIf { it >= 0 }