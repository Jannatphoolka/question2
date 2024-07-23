package com.example.q2

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var spinner: Spinner
    private lateinit var button: Button
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textViewQ2: TextView = findViewById(R.id.textViewQ2)
        val data = intent.getStringExtra("dataKey")
        textViewQ2.text = data
        spinner = findViewById(R.id.spinner)
        button = findViewById(R.id.button)
        listView = findViewById(R.id.listView)
        val buttonq3: Button = findViewById(R.id.buttonq3)

        val recipes = resources.getStringArray(R.array.recipes)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, recipes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        button.setOnClickListener {
            val selectedRecipe = spinner.selectedItem.toString()
            val recipeDetails = getRecipeDetails(selectedRecipe)
            val ingredients = recipeDetails.split("\n").drop(1) // Drop the title line
            val checklistAdapter = ColoredArrayAdapter(this, ingredients)
            listView.adapter = checklistAdapter
        }
        buttonq3.setOnClickListener {
            val intentQ3 = Intent().setClassName("com.example.q3", "com.example.q3.MainActivity")
            intentQ3.putExtra("dataKey", "Hello Q3")
            startActivity(intentQ3)
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // Optional: Handle selection if needed
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Optional: Handle no selection if needed
            }
        }
    }

    private fun getRecipeDetails(recipe: String): String {
        return when (recipe) {
            "Fusilli Pasta" -> getString(R.string.pasta_recipe)
            "Mushroom Pizza" -> getString(R.string.pizza_recipe)
            "Greek Salad" -> getString(R.string.salad_recipe)
            else -> getString(R.string.select_recipe_prompt)
        }
    }

    inner class ColoredArrayAdapter(context: Context, private val ingredients: List<String>) :
        ArrayAdapter<String>(context, R.layout.colored_item, ingredients) {

        private val checkedStates = BooleanArray(ingredients.size)

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: LayoutInflater.from(context)
                .inflate(R.layout.colored_item, parent, false)
            val checkBox: CheckBox = view.findViewById(R.id.ingredientCheckBox)
            checkBox.text = ingredients[position]

            // Alternate background color
            val color = if (position % 2 == 0) {
                Color.parseColor("#C1C1FF") // light blue
            } else {
                Color.TRANSPARENT // no color
            }
            view.setBackgroundColor(color)

            // Set the checkbox state and apply strikethrough if checked
            checkBox.setOnCheckedChangeListener(null) // Remove any existing listener
            checkBox.isChecked = checkedStates[position]
            applyStrikethrough(checkBox, checkedStates[position])

            // Set the new listener
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                checkedStates[position] = isChecked
                applyStrikethrough(checkBox, isChecked)
            }

            return view
        }

        private fun applyStrikethrough(checkBox: CheckBox, isChecked: Boolean) {
            checkBox.paintFlags = if (isChecked) {
                checkBox.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                checkBox.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }

    }
}
