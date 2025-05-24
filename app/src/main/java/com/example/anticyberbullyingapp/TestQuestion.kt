data class TestQuestion(
    val id: Int,
    val text: String,
    val answers: List<String> = listOf(
        "Ніколи",
        "Рідко",
        "Іноді",
        "Часто",
        "Завжди"
    )
)