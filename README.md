# MyOtusHomeWork
Команда запуска тестов через консоль:

`
mvn clean test -Dbrowser="chrome" -Dfilter="QA" -Dtest=FindCourseTest
`
или через UI Idea:

в Edit Configuration в поле VM Options прописать -Dbrowser="chrome" -Dfilter="QA"

где 

**-Dbrowser** - имя браузера;

**-Dfilter** - фильтр по имени курсов; Например, выбрать все курсы, в имени которых есть 'QA'"
