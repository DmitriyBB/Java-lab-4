# Задачник: исключения и JUnit 5

## Запуск

Из папки `Lab 4`:

```
javac -d out -cp lib/junit-platform-console-standalone.jar $(find src test -name "*.java")
```

Задачи 1–7 (вместо `task01` подставь нужную):

```
java -cp out task01.Main
```

Все тесты (задачи 8–15):

```
java -jar lib/junit-platform-console-standalone.jar execute -cp out --scan-classpath
```

Только одна задача: `--select-package task15` вместо `--scan-classpath`.

## Где что

| Задача | Код | Тесты |
|---|---|---|
| 1–7 | `src/task01` … `src/task07` (`Main`) | — |
| 8 | `src/coursehub/Course.java`, `pom.xml` | `test/task08` |
| 9, 10, 11 | `src/coursehub` | `test/task09`, `test/task10`, `test/task11` |
| 12 | `src/task12/CourseFileStore.java` | `test/task12` |
| 13 | `src/task13/EnrollmentRules.java` | `test/task13` |
| 14 | `src/task14` | `test/task14` |
| 15 | `src/task15` | `test/task15` (27 тестов) |

## Ответы на вопросы

**Задача 1.** Тип `NullPointerException`, место `level4`, путь `main -> level1 -> level2 -> level3 -> level4`.
Причина: `Map.get` вернул `null`, который распаковался в `int`. Исправление в `level4Fixed`: проверка там, где появляется null.

**Задача 4.** `finally` выполняется всегда, перед выходом из метода. `return` внутри `finally` заменяет
и уже вычисленный `return`, и летящее исключение, поэтому ошибка пропадает (пример 5 в выводе).

**Задача 7.** `findCourse` -> `Optional`, `parseCourse` -> типизированный Result, `enroll` -> checked,
`calculatePrice` -> unchecked. Реализованы два контрастных случая: `Optional` и `IllegalArgumentException`.

**Задача 9.** Обычное значение (например, 40) проходит, даже если проверка написана как `<` вместо `<=`
или её нет совсем. Контракт задают именно границы: min−1, min, max, max+1.

**Задача 12.** Что исправлено:
- порядок: каждый тест создаёт свой `Course` в `@BeforeEach`, а `MethodOrderer.Random` это проверяет;
- static state: общих изменяемых static-полей нет;
- текущая директория: файлы пишутся в `@TempDir`, а не в `new File("courses.txt")`;
- `toString`: проверяются поля (`getCode`, `getDurationHours`), а не полная строка.

**Задача 13.** Таблица решений. Правила проверяются по приоритету: закрыт -> повтор -> prerequisite -> места.

| Курс открыт | Есть места | Prerequisite | Повторная | Результат |
|---|---|---|---|---|
| да | да | да | нет | ACCEPTED |
| нет | * | * | * | COURSE_CLOSED |
| да | * | * | да | ALREADY_APPLIED |
| да | * | нет | нет | PREREQUISITE_MISSING |
| да | нет | да | нет | NO_SEATS |

Из 16 комбинаций оставлено 8 тестов: на каждое правило, плюс проверки приоритета, когда нарушено сразу
несколько условий. Остальные комбинации («закрыт» при разных значениях других флагов и т.п.)
проверяют то же правило и новых веток не покрывают.

**Задача 15.** Какие исключения ловятся на каждом уровне:

| Вид ошибки | Тип | Где ловится | Код ошибки |
|---|---|---|---|
| Доменная валидация | `ValidationException` (unchecked) | `CourseHubApi` | `INVALID_INPUT` |
| Ожидаемый отказ | `EnrollmentRefusedException` (checked) | `CourseHubApi` | `COURSE_NOT_FOUND`, `COURSE_FULL`, `ALREADY_ENROLLED` |
| Инфраструктура | `IOException` -> `InfrastructureException` (cause сохранён) | `CourseHubService` переводит, `CourseHubApi` ловит | `SOURCE_UNAVAILABLE` |
| Programming error | `NullPointerException`, `IllegalStateException` | нигде | — (падает) |

- `Course` только бросает исключения и ничего не ловит.
- `CourseHubService` ловит `IOException` (переводит в `InfrastructureException`), а также `NumberFormatException`
  и `ValidationException` (добавляет номер записи, cause сохраняет). Reader закрывается через try-with-resources,
  каталог публикуется только после успешного чтения всего источника.
- `CourseHubApi` ловит только три своих типа и отдаёт стабильный `ErrorCode`. Детали инфраструктуры наружу не попадают.

Почему нельзя `catch (Exception)`: он поймал бы и баги (NPE, IllegalStateException) и превратил их
в «обычный» код ошибки. Баг бы спрятался, а вызывающий получил бы неверный ответ.
Тест `programmingError_isNotHiddenByApi` проверяет, что баг доходит до вызывающего кода.
