package com.example.quizz_app_basic.core

object QuestionRepository {

    fun getAll(): List<Question> {
        return listOf(
            Question(
                1,
                "Historia",
                "¿Quién descubrió América?",
                "Cristóbal Colón",
                listOf("Napoleón", "Einstein", "Newton")
            ),
            Question(
                2,
                "Historia",
                "¿En qué año comenzó la Segunda Guerra Mundial?",
                "1939",
                listOf("1914", "1945", "1929")
            ),
            Question(
                3,
                "Historia",
                "¿Qué civilización construyó Machu Picchu?",
                "Inca",
                listOf("Maya", "Azteca", "Romana")
            ),
            Question(
                4,
                "Historia",
                "¿Quién fue el primer presidente de Estados Unidos?",
                "George Washington",
                listOf("Thomas Jefferson", "Abraham Lincoln", "John Adams")
            ),
            Question(
                5,
                "Historia",
                "¿Qué muro cayó en 1989?",
                "Muro de Berlín",
                listOf("Muralla China", "Muro de Adriano", "Muro de las Lamentaciones")
            ),
            Question(
                6,
                "Historia",
                "¿Qué país inició la Revolución Industrial?",
                "Inglaterra",
                listOf("Francia", "Alemania", "Estados Unidos")
            ),

            Question(
                7,
                "Música",
                "¿Qué instrumento tiene 88 teclas?",
                "Piano",
                listOf("Violín", "Flauta", "Guitarra")
            ),
            Question(
                8,
                "Música",
                "¿Qué banda lanzó el álbum 'Abbey Road'?",
                "The Beatles",
                listOf("Queen", "The Rolling Stones", "Nirvana")
            ),
            Question(
                9,
                "Música",
                "¿Quién es conocido como el 'Rey del Pop'?",
                "Michael Jackson",
                listOf("Elvis Presley", "Freddie Mercury", "Bruno Mars")
            ),
            Question(
                10,
                "Música",
                "¿Cuál es la clave musical más común para voces agudas?",
                "Clave de sol",
                listOf("Clave de fa", "Clave de do", "Clave de percusión")
            ),
            Question(
                11,
                "Música",
                "¿Qué cantante colombiana interpreta 'Hips Don’t Lie'?",
                "Shakira",
                listOf("Karol G", "Rosalía", "Paulina Rubio")
            ),
            Question(
                12,
                "Música",
                "¿Cuántas cuerdas tiene una guitarra clásica?",
                "6",
                listOf("4", "5", "7")
            ),

            Question(
                13,
                "Deportes",
                "¿Cuántos jugadores por equipo hay en la cancha de fútbol?",
                "11",
                listOf("9", "10", "12")
            ),
            Question(
                14,
                "Deportes",
                "¿En qué deporte se usa una raqueta y un volante?",
                "Bádminton",
                listOf("Tenis", "Squash", "Tenis de mesa")
            ),
            Question(
                15,
                "Deportes",
                "¿Qué país ganó el Mundial de Fútbol 2022?",
                "Argentina",
                listOf("Francia", "Brasil", "Alemania")
            ),
            Question(
                16,
                "Deportes",
                "¿Cuántos puntos vale un triple en baloncesto?",
                "3",
                listOf("1", "2", "4")
            ),
            Question(
                17,
                "Deportes",
                "¿Qué deporte practica Rafael Nadal?",
                "Tenis",
                listOf("Golf", "Fórmula 1", "Ciclismo")
            ),
            Question(
                18,
                "Deportes",
                "¿Cómo se llama la competencia más famosa del ciclismo de ruta?",
                "Tour de Francia",
                listOf("Giro de Italia", "Vuelta a España", "Paris-Roubaix")
            ),

            Question(
                19,
                "Cultura General",
                "¿Cuál es el planeta más grande del sistema solar?",
                "Júpiter",
                listOf("Saturno", "Marte", "Venus")
            ),
            Question(
                20,
                "Cultura General",
                "¿Cuál es el océano más grande del mundo?",
                "Océano Pacífico",
                listOf("Océano Atlántico", "Océano Índico", "Océano Ártico")
            ),
            Question(
                21,
                "Cultura General",
                "¿Cuál es la capital de Japón?",
                "Tokio",
                listOf("Kioto", "Seúl", "Pekín")
            ),
            Question(
                22,
                "Cultura General",
                "¿Cuántos continentes se reconocen comúnmente?",
                "6",
                listOf("5", "7", "8")
            ),
            Question(
                23,
                "Cultura General",
                "¿Qué gas respiramos principalmente para vivir?",
                "Oxígeno",
                listOf("Hidrógeno", "Helio", "Dióxido de carbono")
            ),
            Question(
                24,
                "Cultura General",
                "¿Qué idioma tiene más hablantes nativos en el mundo?",
                "Chino mandarín",
                listOf("Inglés", "Español", "Árabe")
            ),

            Question(
                25,
                "Entretenimiento",
                "¿Cómo se llama el mago protagonista de una saga creada por J.K. Rowling?",
                "Harry Potter",
                listOf("Frodo Bolsón", "Percy Jackson", "Sherlock Holmes")
            ),
            Question(
                26,
                "Entretenimiento",
                "¿En qué saga aparece el personaje Darth Vader?",
                "Star Wars",
                listOf("Star Trek", "Marvel", "DC")
            ),
            Question(
                27,
                "Entretenimiento",
                "¿Qué plataforma es conocida por series como 'Stranger Things'?",
                "Netflix",
                listOf("HBO Max", "Disney+", "Prime Video")
            ),
            Question(
                28,
                "Entretenimiento",
                "¿Qué videojuego popular incluye construir con bloques en mundo abierto?",
                "Minecraft",
                listOf("Fortnite", "Roblox", "The Sims")
            ),
            Question(
                29,
                "Entretenimiento",
                "¿Qué compañía creó al personaje Mario Bros?",
                "Nintendo",
                listOf("Sega", "Sony", "Microsoft")
            ),
            Question(
                30,
                "Entretenimiento",
                "¿Qué premio reconoce lo mejor del cine en Hollywood?",
                "Premios Óscar",
                listOf("Premios Grammy", "Premios Emmy", "Globos de Oro")
            )
        )
    }
}
