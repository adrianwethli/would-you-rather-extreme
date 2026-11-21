package com.wouldyouratherextreme.game.data

import android.content.res.AssetManager
import com.wouldyouratherextreme.game.domain.QuestionPack

/**
 * Loads question packs that are stored inside the app (app/src/main/assets/questions),
 * so the game works fully offline.
 */
class QuestionRepository(private val assets: AssetManager) {

    /** The 50 free questions. */
    fun loadFreePack(): QuestionPack = load("questions/free_pack.json")

    private fun load(path: String): QuestionPack {
        val text = assets.open(path).bufferedReader(Charsets.UTF_8).use { it.readText() }
        return QuestionPackParser.parse(text)
    }
}
