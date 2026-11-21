package com.wouldyouratherextreme.game.data

import android.content.res.AssetManager
import com.wouldyouratherextreme.game.domain.QuestionLibrary
import com.wouldyouratherextreme.game.domain.QuestionPack

/**
 * Loads question packs that are stored inside the app (app/src/main/assets/questions),
 * so the game works fully offline.
 *
 * - `free_pack.json` is the free pack.
 * - Every file named `premium_pack_*.json` is a premium pack. To add another 50 questions later,
 *   just add a new file such as `premium_pack_02.json`; the app finds it automatically.
 */
class QuestionRepository(private val assets: AssetManager) {

    fun loadLibrary(): QuestionLibrary {
        val free = loadFreePack()
        require(!free.isPremium) { "free_pack.json must not be marked as premium." }
        val premiumPacks = loadPremiumPacks()
        require(premiumPacks.all { it.isPremium }) { "Every premium_pack_*.json must be marked as premium." }
        return QuestionLibrary(
            free = free.questions,
            premium = premiumPacks.flatMap { it.questions },
        )
    }

    private fun loadFreePack(): QuestionPack = load("$FOLDER/free_pack.json")

    private fun loadPremiumPacks(): List<QuestionPack> =
        (assets.list(FOLDER) ?: emptyArray())
            .filter { it.startsWith("premium_pack_") && it.endsWith(".json") }
            .sorted()
            .map { load("$FOLDER/$it") }

    private fun load(path: String): QuestionPack {
        val text = assets.open(path).bufferedReader(Charsets.UTF_8).use { it.readText() }
        return QuestionPackParser.parse(text)
    }

    private companion object {
        const val FOLDER = "questions"
    }
}
