package com.example.cipher.ui.gallery

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
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.example.cipher.R
import com.example.cipher.databinding.FragmentGalleryBinding

class GalleryFragment : Fragment() {
    private var _binding: FragmentGalleryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)
        val word: EditText = root.findViewById(R.id.slovoShifrovaniya)
        val key: EditText = root.findViewById(R.id.key)
        var answer: TextView = root.findViewById(R.id.answer)
        val encrypt: Button = root.findViewById(R.id.encrypt)
        val decrypt: Button = root.findViewById(R.id.decrypt)
        var language: ToggleButton = root.findViewById(R.id.language)
        val copyButton: Button = root.findViewById(R.id.copyButton1)

        language.setOnCheckedChangeListener { buttonView, isChecked -> if (isChecked) {
            encrypt.setOnClickListener {
                var text = word.text.toString().trim()
                val encryptedText =
                    context?.let { it1 -> checkInputNumber(it1,key.text.toString()) }?.let { it2 -> encrypt(text, it2)
                    }
                answer.text = encryptedText
                word.setText("")
            }
            decrypt.setOnClickListener {
                var text = word.text.toString()
                val decryptedText =
                    context?.let { it1 -> checkInputNumber(it1,key.text.toString()) }?.let { it2 ->
                        decrypt(text,
                            it2
                        )
                    }
                answer.text = decryptedText
                word.setText("")
            }
        } else {
            encrypt.setOnClickListener {
                var text = word.text.toString().trim()
                val encryptedText =
                    context?.let { it1 -> checkInputNumber(it1,key.text.toString()) }?.let { it2 ->
                        ruEncrypt(text,
                            it2
                        )
                    }
                answer.text = encryptedText
                word.setText("")
            }
            decrypt.setOnClickListener {
                var text = word.text.toString()
                val decryptedText =
                    context?.let { it1 -> checkInputNumber(it1,key.text.toString()) }?.let { it2 ->
                        ruDecrypt(text,
                            it2
                        )
                    }
                answer.text = decryptedText
                word.setText("")
            }
        }
        }
        encrypt.setOnClickListener {
            var text = word.text.toString().trim()
            val encryptedText =
                context?.let { it1 -> checkInputNumber(it1,key.text.toString()) }?.let { it2 ->
                    ruEncrypt(text,
                        it2
                    )
                }
            answer.text = encryptedText
            word.setText("")
        }
        decrypt.setOnClickListener {
            var text = word.text.toString()
            val decryptedText =
                context?.let { it1 -> checkInputNumber(it1,key.text.toString()) }?.let { it2 ->
                    ruDecrypt(text,
                        it2
                    )
                }
            answer.text = decryptedText
            word.setText("")
        }
        copyButton.setOnClickListener {
            val textToCopy = answer.text.toString()
            val clipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("text", textToCopy)
            clipboardManager.setPrimaryClip(clipData)
            Toast.makeText(context, "Текст скопирован", Toast.LENGTH_SHORT).show()
        }
            return root
        }
        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }


        fun encrypt(word: String, key: Int): String {
            context?.let { checkInput(it, word) }
            val sb = StringBuilder()
            for (element in word) {
                if (element.isLetter()) {
                    if (element.isLowerCase()) {
                        val lowerArray = arrayOf(
                            'a',
                            'b',
                            'c',
                            'd',
                            'e',
                            'f',
                            'g',
                            'h',
                            'i',
                            'j',
                            'k',
                            'l',
                            'm',
                            'n',
                            'o',
                            'p',
                            'q',
                            'r',
                            's',
                            't',
                            'u',
                            'v',
                            'w',
                            'x',
                            'y',
                            'z'
                        )
                        if (element !in lowerArray) {
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
                        val index = lowerArray.indexOf(element)
                        var newIndex = index + key
                        while (newIndex >= 26) {
                            newIndex -= 26
                        }
                        sb.append(lowerArray[newIndex])
                    }
                    if (element.isUpperCase()) {
                        val upperArray = arrayOf(
                            'A',
                            'B',
                            'C',
                            'D',
                            'E',
                            'F',
                            'G',
                            'H',
                            'I',
                            'J',
                            'K',
                            'L',
                            'M',
                            'N',
                            'O',
                            'P',
                            'Q',
                            'R',
                            'S',
                            'T',
                            'U',
                            'V',
                            'W',
                            'X',
                            'Y',
                            'Z'
                        )
                        if (element !in upperArray) {
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
                        val index = upperArray.indexOf(element)
                        var newIndex = index + key
                        while (newIndex >= 26) {
                            newIndex -= 26
                        }
                        sb.append(upperArray[newIndex])
                    }
                } else {
                    sb.append(element)
                }
            }
            return sb.toString()
        }


        fun decrypt(word: String, key: Int): String {
            var key1 = key
            while (key1 >= 26) {
                key1 -= 26
            }
            return encrypt(word, 26 - key1)
        }

        fun ruEncrypt(word: String, key: Int): String {
            context?.let { checkInput(it, word) }
            val sb = StringBuilder()
            for (element in word) {
                if (element.isLetter()) {
                    if (element.isLowerCase()) {
                        val lowerArray = arrayOf(
                            'а',
                            'б',
                            'в',
                            'г',
                            'д',
                            'е',
                            'ё',
                            'ж',
                            'з',
                            'и',
                            'й',
                            'к',
                            'л',
                            'м',
                            'н',
                            'о',
                            'п',
                            'р',
                            'с',
                            'т',
                            'у',
                            'ф',
                            'х',
                            'ц',
                            'ч',
                            'ш',
                            'щ',
                            'ъ',
                            'ы',
                            'ь',
                            'э',
                            'ю',
                            'я'
                        )
                        if (element !in lowerArray) {
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
                        val index = lowerArray.indexOf(element)
                        var newIndex = index + key
                        while (newIndex >= 33) {
                            newIndex -= 33
                        }
                        sb.append(lowerArray[newIndex])
                    }
                    if (element.isUpperCase()) {
                        val upperArray = arrayOf(
                            'А',
                            'Б',
                            'В',
                            'Г',
                            'Д',
                            'Е',
                            'Ё',
                            'Ж',
                            'З',
                            'И',
                            'Й',
                            'К',
                            'Л',
                            'М',
                            'Н',
                            'О',
                            'П',
                            'Р',
                            'С',
                            'Т',
                            'У',
                            'Ф',
                            'Х',
                            'Ц',
                            'Ч',
                            'Ш',
                            'Щ',
                            'Ъ',
                            'Ы',
                            'Ь',
                            'Э',
                            'Ю',
                            'Я'
                        )
                        if (element !in upperArray) {
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
                        val index = upperArray.indexOf(element)
                        var newIndex = index + key
                        while (newIndex >= 33) {
                            newIndex -= 33
                        }
                        sb.append(upperArray[newIndex])
                    }
                } else {
                    sb.append(element)
                }
            }
            return sb.toString()
        }


        fun ruDecrypt(word: String, key: Int): String {
            var key1 = key
            while (key1 >= 33) {
                key1 -= 33
            }
            return ruEncrypt(word, 33 - key1)
        }

        fun checkInput(context: Context, input: String) {
            if (input.isBlank()) {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Внимание!")
                builder.setMessage("Вы не ввели строку.")
                builder.setPositiveButton("ОК") { dialog, _ ->
                    dialog.dismiss()
                }
                val dialog = builder.create()
                dialog.show()
            }
        }

        fun checkInputNumber(context: Context, number: String): Int {
            if (number.isBlank()) {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Внимание!")
                builder.setMessage("Вы не ввели число.")
                builder.setPositiveButton("ОК") { dialog, _ ->
                    dialog.dismiss()
                }
                val dialog = builder.create()
                dialog.show()
            } else {
                return number.toInt()
            }
            return 0
        }
    }