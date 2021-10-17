package com.example.roomdatabasezero

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdatabasezero.adapter.WordListAdapter
import com.example.roomdatabasezero.model.Word
import com.example.roomdatabasezero.model.WordViewModel
import com.example.roomdatabasezero.model.WordViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {

    private val wordViewModel: WordViewModel by viewModels {
        WordViewModelFactory((application as WordApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = WordListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        wordViewModel.allWords.observe(this, { words ->
            // Update the cached copy of the words in the adapter.
            words?.let { adapter.submitList(it) }
        })

        val waitingForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.data != null && result.resultCode == RESULT_OK) {
                    result.data?.getStringExtra(NewWordActivity.EXTRA_REPLY)?.let {
                        val word = Word(it)
                        wordViewModel.insert(word)
                    }
                } else {
                    Toast.makeText(this, R.string.empty_not_saved, Toast.LENGTH_LONG).show()
                }
            }

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            waitingForResult.launch(Intent(this, NewWordActivity::class.java))
        }
    }
}