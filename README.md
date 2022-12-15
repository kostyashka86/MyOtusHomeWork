# homework4

Команда запуска тестов через консоль:

`mvn clean test -Dbrowser="chrome" -Dfilter="QA"`

или через UI Idea:

в `Edit Configuration` в поле `VM Options` прописать `-Dbrowser="chrome" -Dfilter="QA"`

где

**-Dbrowser** - имя браузера;

**-Dfilter** - фильтр по имени курсов; Например, выбрать все курсы, в имени которых есть 'QA'

---

1. Стартуем первый экземпляр Selenoid

````
docker start selenoid_1
````

2. Стартуем второй экземпляр Selenoid

````
docker start selenoid_2 
````

3. Стартуем балансировщик GGR из директории <рабочая_директория>/ggr_config командой:

````
start /b ggr_windows_amd64.exe -guests-allowed -guests-quota "test" -verbose C:\Users\k.shishmagaev\selenoid\ggr_config\quota
````

4. Стартуем модуль GGR-UI из директории <рабочая_директория>/ggr_config командой:

````
start /b ggr-ui_windows_amd64.exe -quota-dir C:\Users\k.shishmagaev\selenoid\ggr_config\quota
````

5. Старутем Selenoid-UI из директории <рабочая_директория>/ggr_config командой:

````
start /b selenoid-ui_windows_amd64.exe --selenoid-uri http://127.0.0.1:8888 -listen ":8090" -allowed-origin "*"