# Приложение Покемоны

Это простое приложение для Android предоставляющее информацию о покемонах. 
Оно позволяет пользователям загружать список покемонов, искать покемонов по имени и просматривать детальную информацию о каждом покемоне. 

## Особенности

- Просмотр списка покемонов.
- Поиск покемонов по имени.
- Детальная информация о покемонах.
- Работа без интернет-соединения.

## Технологии

- Clean Architecture
- MVVM (Model-View-ViewModel)
- Retrofit 2 для сетевых запросов
- Room для локального хранения данных
- Paging 3 с RemoteMediator для пагинации
- Dagger 2 для внедрения зависимостей
- OkHttp для отслеживания HTTP запросов
- Jetpack Navigation Component для навигации между экранами
- Palette для извлечения цветов из изображений
- Glide для загрузки и кэширования изображений

## Примечания

- Время поиска по имени может быть увеличено, т.к. поиск в приложении осуществляется по всем страницам, предоставляемым API, в связи с тем что в самом API нет функции поиска по имени.
- Реализация Clean Architecture выполнена с некоторыми упрощениями для удобства разработки.


## Скриншоты

![Screenshot1](https://github.com/sitegit/Pokemons/assets/47815702/44a635b3-26a1-4ce4-9734-3fb36f3ed5b4)
![Screenshot5](https://github.com/sitegit/Pokemons/assets/47815702/df1313e8-7e97-4687-af49-c23e112715c6)
![Screenshot4](https://github.com/sitegit/Pokemons/assets/47815702/3aaea3c2-3c08-465c-9cbc-eec0bd9b4594)
![Screenshot2](https://github.com/sitegit/Pokemons/assets/47815702/2c939dda-fe88-4b8a-a3d7-86a3806e24dc)
![Screenshot6](https://github.com/sitegit/Pokemons/assets/47815702/284daf3b-6907-4caf-8eaa-c7648e268804)
![Screenshot3](https://github.com/sitegit/Pokemons/assets/47815702/fc183b1c-ce3c-445e-a6b1-cede920784d3)

