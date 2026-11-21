package com.wouldyouratherextreme.game.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** Checks all question files together, including any premium pack added in the future. */
class AllPacksFileTest {

    private val packsByFile = TestAssets.jsonFileNames()
        .associateWith { QuestionPackParser.parse(TestAssets.read(it)) }

    @Test
    fun freeAndPremiumFilesAreMarkedCorrectly() {
        packsByFile.forEach { (file, pack) ->
            when {
                file == "free_pack.json" -> assertFalse("$file must not be premium", pack.isPremium)
                file.startsWith("premium_pack_") -> assertTrue("$file must be premium", pack.isPremium)
                else -> error("Unexpected file in the questions folder: $file")
            }
        }
    }

    @Test
    fun everyQuestionIdIsUniqueAcrossAllPacks() {
        val ids = packsByFile.values.flatMap { pack -> pack.questions.map { it.id } }
        assertEquals("Duplicate ids across packs", ids.size, ids.toSet().size)
    }

    @Test
    fun thereIsExactlyOneFreePackAndAtLeastOnePremiumPack() {
        assertEquals(1, packsByFile.keys.count { it == "free_pack.json" })
        assertTrue(packsByFile.keys.any { it.startsWith("premium_pack_") })
    }
}
