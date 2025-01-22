package com.example.cipher.ui.home

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import com.example.cipher.R
import com.example.cipher.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val word: EditText = root.findViewById(R.id.slovoShifrovaniya)
        val key: EditText = root.findViewById(R.id.key)
        var answer: TextView = root.findViewById(R.id.answer)
        val encrypt: Button = root.findViewById(R.id.encrypt)
        val decrypt: Button = root.findViewById(R.id.decrypt)
        var language: ToggleButton = root.findViewById(R.id.language)
        val copyButton: Button = root.findViewById(R.id.copyButton1)
        val russianAlphabetLowerCase = listOf('а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я')
        val russianAlphabetUpperCase = russianAlphabetLowerCase.map { it.uppercaseChar() }
        val englishLowercaseAlphabet = ('a'..'z').toList()
        val englishUppercaseAlphabet = ('A'..'Z').toList()
        language.setOnCheckedChangeListener { buttonView, isChecked ->
            // английский
            if (isChecked) {
                // шифровать
                encrypt.setOnClickListener {
                    var text = word.text.toString().trim()
                    var key1 = key.text.toString().replace("\\s".toRegex(), "")
                    val encryptedText = vigenereEncrypt(text, key1, englishLowercaseAlphabet, englishUppercaseAlphabet)
                    answer.text = encryptedText
                    word.setText("")
                }
                // дешифровать
                decrypt.setOnClickListener {
                    var text = word.text.toString().trim()
                    var key1 = key.text.toString().replace("\\s".toRegex(), "")
                    val decryptedText = vigenereDecrypt(text, key1, englishLowercaseAlphabet, englishUppercaseAlphabet)
                    answer.text = decryptedText
                    word.setText("")
                }
                } else { // русский
                encrypt.setOnClickListener {//зашифровать
                    var text = word.text.toString().trim()
                    var key1 = key.text.toString().replace("\\s".toRegex(), "")
                    val encryptedText = vigenereEncrypt(text, key1, russianAlphabetLowerCase, russianAlphabetUpperCase)
                    answer.text = encryptedText
                    word.setText("")
                }
                // дешифровать
                decrypt.setOnClickListener {
                    var text = word.text.toString().trim()
                    var key1 = key.text.toString().replace("\\s".toRegex(), "")
                    val decryptedText = vigenereDecrypt(text, key1, russianAlphabetLowerCase, russianAlphabetUpperCase)
                    answer.text = decryptedText
                    word.setText("")
                }
            }
            }
        copyButton.setOnClickListener {
            val textToCopy = answer.text.toString()
            val clipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("text", textToCopy)
            clipboardManager.setPrimaryClip(clipData)
            Toast.makeText(context, "Текст скопирован", Toast.LENGTH_SHORT).show()
        }
        encrypt.setOnClickListener {
            var text = word.text.toString().trim()
            var key1 = key.text.toString().replace("\\s".toRegex(), "")
            val encryptedText = vigenereEncrypt(text, key1, russianAlphabetLowerCase, russianAlphabetUpperCase)
            answer.text = encryptedText
            word.setText("")
        }
        decrypt.setOnClickListener {
            var text = word.text.toString().trim()
            var key1 = key.text.toString().replace("\\s".toRegex(), "")
            val decryptedText = vigenereDecrypt(text, key1, russianAlphabetLowerCase, russianAlphabetUpperCase)
            answer.text = decryptedText
            word.setText("")
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun vigenereEncrypt(input: String, key: String, lowercaseAlphabet: List<Char>, uppercaseAlphabet: List<Char>): String {
        context?.let { checkInput(it,key) }
        context?.let { checkInput(it,input) }
        for (element in input) {
            if (element.isLetter()) {
                if ((element !in lowercaseAlphabet) and (element !in uppercaseAlphabet)) {
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Внимание!")
                    builder.setMessage("Вы ввели букву из другого алфавита.")
                    builder.setPositiveButton("ОК") { dialog, _ ->
                        dialog.dismiss()
                    }
                    val dialog = builder.create()
                    dialog.show()
                    break
                }
            }
        }
        for (element in key) {
            if (element.isLetter()) {
                if ((element !in lowercaseAlphabet) and (element !in uppercaseAlphabet)) {
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Внимание!")
                    builder.setMessage("Вы ввели букву из другого алфавита в код.")
                    builder.setPositiveButton("ОК") { dialog, _ ->
                        dialog.dismiss()
                    }
                    val dialog = builder.create()
                    dialog.show()
                    break
                }
            }
        }
        val encryptedText = StringBuilder()
        var keyIndex = 0
        for (char in input) {
            val alphabet = if (char.isUpperCase()) uppercaseAlphabet else lowercaseAlphabet
            val shift = alphabet.indexOf(key[keyIndex].lowercaseChar())
            val index = alphabet.indexOf(char.lowercaseChar())
            if (index != -1) {
                val encryptedIndex = (index + shift) % alphabet.size
                encryptedText.append(if (char.isUpperCase()) alphabet[encryptedIndex].uppercaseChar() else alphabet[encryptedIndex])
            } else {
                encryptedText.append(char)
                continue
            }
            keyIndex = (keyIndex + 1) % key.length
        }
        return encryptedText.toString()
    }


    fun vigenereDecrypt(input: String, key: String, lowercaseAlphabet: List<Char>, uppercaseAlphabet: List<Char>): String {
        context?.let { checkInput(it,input) }
        context?.let { checkInput(it,key) }
        for (element in input) {
            if (element.isLetter()) {
                if ((element !in lowercaseAlphabet) and (element !in uppercaseAlphabet)) {
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Внимание!")
                    builder.setMessage("Вы ввели букву из другого алфавита.")
                    builder.setPositiveButton("ОК") { dialog, _ ->
                        dialog.dismiss()
                    }
                    val dialog = builder.create()
                    dialog.show()
                    break
                }
            }
        }
        for (element in key) {
            if (element.isLetter()) {
                if ((element !in lowercaseAlphabet) and (element !in uppercaseAlphabet)) {
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Внимание!")
                    builder.setMessage("Вы ввели букву из другого алфавита.")
                    builder.setPositiveButton("ОК") { dialog, _ ->
                        dialog.dismiss()
                    }
                    val dialog = builder.create()
                    dialog.show()
                    break
                }
            }
        }
        val decryptedText = StringBuilder()
        var keyIndex = 0
        for (char in input) {
            val alphabet = if (char.isUpperCase()) uppercaseAlphabet else lowercaseAlphabet
            val shift = lowercaseAlphabet.indexOf(key[keyIndex].lowercaseChar())
            val index = alphabet.indexOf(char.lowercaseChar())
            if (index != -1) {
                val decryptedIndex = (index - shift + alphabet.size) % alphabet.size
                decryptedText.append(if (char.isUpperCase()) alphabet[decryptedIndex].uppercaseChar() else alphabet[decryptedIndex])
            } else {
                decryptedText.append(char)
                continue
            }
            keyIndex = (keyIndex + 1) % key.length
        }

        return decryptedText.toString()
    }

    fun checkInput(context: Context, input: String) {
        if (input.isBlank()) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Внимание!")
            builder.setMessage("Вы ничего не ввели в строку.")
            builder.setPositiveButton("ОК") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }
    }
}
