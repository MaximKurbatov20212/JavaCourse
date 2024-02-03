# Задание 1. bencode parser (10 баллов)

Написать bencode парсер, который будет принимать файл в bencode формате, переводить его в json-представление и выводить в консоль или файл.

Запуск программы:

java Main.java <filename> [output-file]

filename - файл с bencode
output-file - файл с читаемым выводом (опциональный аргумент)

Требования к программе:

Программа должна содержать юнит-тесты для парсера и лексера
Ошибки парсера / лексера должны быть понятны пользователю. Пример понятной ошибки: Expected token: CURLY_BRACE, got COMMA at line 5.
json должны содержать отступы при переходе на следующий уровень вложенности


# Задание 2. Игра (15 баллов)

Написать игру с использованием Swing / JavaFx. Архитектура программы должна быть основана на паттерне Model-View-Controller / Model-View-Presenter / Model-View-View Model. 

Требования

Игра должна поддерживать таблицу рекордов.
Пользователю должны быть доступны команды: Exit, About, New Game, High Scores.
Должны быть тесты на Model
Программа должна использовать систему сборки gradle

Варианты игр

https://ru.wikipedia.org/wiki/Sokoban
https://hexxagon.com
https://ru.wikipedia.org/wiki/Digger
https://ru.wikipedia.org/wiki/Arkanoid
https://ru.wikipedia.org/wiki/Paratrooper
https://en.wikipedia.org/wiki/Galaga
https://agar.io
https://phylo.cs.mcgill.ca/play.php
https://en.wikipedia.org/wiki/2048_(video_game)
https://en.wikipedia.org/wiki/Wordle
https://en.wikipedia.org/wiki/Sudoku
https://flappybird.io/
ваш вариант (предварительно согласовать)

Забаненные игры

Пакман
Морской бой
Три в ряд
Тетрис
Сапер
