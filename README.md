# ___YumYum___  [<img src="https://cdn-icons-png.flaticon.com/512/4063/4063869.png" width="25">](https://www.mediafire.com/file/jse8cdr6v70az2v/YumYum.apk/file)

___ `YumYum` ___ is android application designed for foodies who enjoy experimenting with various meals.

> Want delicious `Recipes` ? YumYum is the perfect app for you.

## Preview

<table>
  <tr>
<td><img src="https://github.com/ahmeddadel/YumYum/blob/master/screentshots/1.png?raw=true" alt="Preview" style="width:300px; height:600px; "></td>
<td><img src="https://github.com/ahmeddadel/YumYum/blob/master/screentshots/2.png?raw=true" alt="Preview" style="width:300px; height:600px; "></td>
  </tr>

<td><img src="https://github.com/ahmeddadel/YumYum/blob/master/screentshots/3.png?raw=true" alt="Preview" style="width:300px; height:600px; "></td>
<td><img src="https://github.com/ahmeddadel/YumYum/blob/master/screentshots/4.png?raw=true" alt="Preview" style="width:300px; height:600px; "></td>
  </tr>

<td><img src="https://github.com/ahmeddadel/YumYum/blob/master/screentshots/5.png?raw=true" alt="Preview" style="width:300px; height:600px; "></td>
<td><img src="https://github.com/ahmeddadel/YumYum/blob/master/screentshots/6.png?raw=true" alt="Preview" style="width:300px; height:600px; "></td>
  </tr>

<td><img src="https://github.com/ahmeddadel/YumYum/blob/master/screentshots/7.png?raw=true" alt="Preview" style="width:300px; height:600px; "></td>
<td><img src="https://github.com/ahmeddadel/YumYum/blob/master/screentshots/8.png?raw=true" alt="Preview" style="width:300px; height:600px; "></td>
  </tr>

<td><img src="https://github.com/ahmeddadel/YumYum/blob/master/screentshots/9.png?raw=true" alt="Preview" style="width:300px; height:600px; "></td>
<td><img src="https://github.com/ahmeddadel/YumYum/blob/master/screentshots/10.png?raw=true" alt="Preview" style="width:300px; height:600px; "></td>
  </tr>
</table>

## Features

* __`YumYum`__ is built on top of the [TheMealDB](https://www.themealdb.com/) API. 

* __`YumYum`__ provides easy access to TheMealDB API, enabling users to search for meals by name and obtain comprehensive meal details, including video instructions on how to prepare them.

* __`YumYum`__ provides a sleek and user-friendly interface, making it effortless for users to navigate the app's different features.

* Save your favourite meals and access them quickly for future reference.

## Architecture

__ `YumYum` __ is built using the following technologies/tools:

* MVVM & LiveData

> Saperate logic code from views and save the state in case the screen configuration changes.

* Navigation component

> one activity contains multiple fragments instead of creating multiple activites.

* Viewbinding

> instead of inflating views manually view binding will take care of that.

* Coroutines

> do some code in the background.

* Retrofit

> making HTTP connection with the rest API and convert meal json file to Kotlin/Java object.

* Room 

> Save meals in local database.

* Repository Design Pattern

> A pattern that separates the application logic from data access logic by abstracting the data source into a set of repository classes, simplifying switching between data sources.

* Singleton Design Pattern

> A pattern that ensures a class has only one instance and provides a global point of access to that instance, useful for objects such as configuration managers, logging services, or database connections.

* Glide

> Catch images and load them in imageView.
