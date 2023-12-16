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
- Основа дизайна взята с канала Philipp Lackner

## Скриншоты

<table>
  <tr>
    <td><img src="https://github.com/sitegit/Pokemons/assets/47815702/44a635b3-26a1-4ce4-9734-3fb36f3ed5b4" width="200" alt="Screenshot1"/></td>
    <td><img src="https://github.com/sitegit/Pokemons/assets/47815702/3aaea3c2-3c08-465c-9cbc-eec0bd9b4594" width="200" alt="Screenshot4"/></td>
  </tr>
  <tr>
    <td><img src="https://github.com/sitegit/Pokemons/assets/47815702/284daf3b-6907-4caf-8eaa-c7648e268804" width="400" alt="Screenshot6"/></td>
  </tr>
  <tr>
    <td><img src="https://github.com/sitegit/Pokemons/assets/47815702/2c939dda-fe88-4b8a-a3d7-86a3806e24dc" width="200" alt="Screenshot2"/></td>
    <td><img src="https://github.com/sitegit/Pokemons/assets/47815702/fc183b1c-ce3c-445e-a6b1-cede920784d3" width="200" alt="Screenshot3"/></td>
  </tr>
</table>


