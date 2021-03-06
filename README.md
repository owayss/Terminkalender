![Logo](http://i.imgur.com/0GoIWlz.png)

Es un sistema que sirve de herramienta para la enseñanza de idiomas a un grupo de alumnos que interactuarán entre si mediante una aplicación. Dicha herramienta se presenta como un juego que tiene un calendario de miércoles a domingo y un chat. Los alumnos tendrán diferentes tareas que realizar conjuntamente a lo largo de la semana y deberán comunicarse entre ellos mediante un chat, así podrán ponerse de acuerdo y anotar esas tareas en el calendario. El método de aprendizaje se basa en el uso del calendario y un chat con el que se pretende que mediante el sistema de comunicación verbal escrito se incremente la interacción y negociación entre los alumnos.

## Aplicaciones

Terminkalender se puede dividir en tres aplicaciones distintas:

  * Aplicación del alumno: Permite a los alumnos conectarse a la sesión del juego anteriormente explicada. Esta desarrollada en [LibGDX](https://github.com/libGDX/libGDX) y para la sincronización con el servidor utiliza [WebSockets](https://github.com/TooTallNate/Java-WebSocket)
  * Aplicación del profesor: Esta aplicación permite al profesor crear las sesiones de juego para los alumnos y recopilar los datos generados por la interacción de estos: las conversaciones de chat y la correcta asignación de las tareas en el calendario. También esta desarrollada en [LibGDX](https://github.com/libGDX/libGDX) y para la sincronización con el servidor utiliza [WebSockets](https://github.com/TooTallNate/Java-WebSocket)
  * Servidor: Se encarga de almacenar los datos e interconectar tanto la aplicación del alumno como la del profesor. Desarrollado en PHP y [Rachet](https://github.com/ratchetphp/Ratchet) para crear el servidor de WebSockets.

## Instalar recursos necesarios

Aqui voy a explicar que se necesita para trabajar en tu propio ordenador con Terminkalender (Yo uso Ubuntu):

#### Aplicación del alumno y del profesor

Las dos aplicaciones están implementadas en el mismo proyecto de [LibGDX](https://github.com/libGDX/libGDX). Para modificarlo y ejecutarlo, se puede seguir la guia que proporciona su página en github para usarlo en [Eclipse](https://github.com/libgdx/libgdx/wiki/Gradle-and-Eclipse), [Intellij IDEA](https://github.com/libgdx/libgdx/wiki/Gradle-and-Intellij-IDEA), [NetBeans](https://github.com/libgdx/libgdx/wiki/Gradle-and-NetBeans), o si no se quiere usar un IDE, por  [linea de comandos](https://github.com/libgdx/libgdx/wiki/Gradle-on-the-Commandline). No hay que olvidar que si se usa uno de los IDEs mencionados anteriormente, hay que instalar en ellos las siguientes [dependencias](https://github.com/libgdx/libgdx/wiki/Setting-up-your-Development-Environment-%28Eclipse%2C-Intellij-IDEA%2C-NetBeans%29). Para la construcción del proyecto se usa [Gradle](http://gradle.org/), así que no hay que preocuparse de las dependencias como [WebSockets](https://github.com/TooTallNate/Java-WebSocket), que automaticamente se añaden cuando se construye el proyecto.

#### Servidor

El servidor se encuentra en el directorio [server](https://github.com/javosuher/Terminkalender/tree/master/server), y para hacerlo funcionar se necesitará tener instalado PHP y MySQL, ya que usa una base de datos para almacenar información. Una guia de instalación para Ubuntu 14.04 sería la [siguiente](https://www.digitalocean.com/community/tutorials/how-to-install-linux-apache-mysql-php-lamp-stack-on-ubuntu-14-04). Para la configuración del usuario de la base de datos, se realiza en el fichero [DataBase.php](https://github.com/javosuher/Terminkalender/blob/master/server/src/MyApp/DataBase.php), y para la base de datos en MySQL se dispone del script [DataBase.sql](https://github.com/javosuher/Terminkalender/blob/master/server/src/MyApp/DataBase.sql). Se puede dejar el que tengo yo o modificar al que se quiera.

Para instalar las dependecias del servidor se usa [Composer](https://getcomposer.org/), así que suponiendo que ya tenemos descargado el fichero composer.phar en nuestro directorio de usuario, se instalarían con el siguiente comando estando situados en [server](https://github.com/javosuher/Terminkalender/tree/master/server):

```
php ~/composer.phar install
```
Para ejecutar el servidor en nuestro PC, estando de nuevo en la carpeta raíz del servidor, se introduce el siguiente comando:

```
php Server.php
```
Cuando queramos dejar nuestro servidor en funcionamiento, lo mas recomendable es ejecutarlo usando [Supervisord](http://supervisord.org/). Supervisor es un demonio que lanza procesos y los mantiene en funcionamiento, y si por alguna razón se detiene la aplicación, este se aseguraria de ejecutarla de nuevo. Para instalar supervisor, se puede hacer mediante herramientas como pip, easy_install, apt-get o yum. Si tenemos Ubuntu se podría instalar con el siguiente comando:

```
sudo apt-get install supervisor
```

Por último, en el directorio del servidor disponemos de un fichero llamado [supervisor.conf](https://github.com/javosuher/Terminkalender/blob/master/server/supervisor.conf) configurado para ejecutarlo con supervisord. Para ello, hay que tener Supervisor instalado previamente y estando en la carpeta raíz del servidor, crear una carpeta llamada 'logs' (Esta carpeta recogerá las salidas que genera el servidor y supervisord, incluyendo los errores), y ejecutarlo con el siguiente comando:

```
sudo supervisord -c supervisor.conf
```
----
Este proyecto está financiado por la Convocatoria de Actuaciones Avaladas para la Mejora Docente del curso 2015/16 de la Universidad de Cádiz, en el marco de la Actuación "__Fomento de la interacción mediante smartphones para la mejora del aprendizaje de lengua extranjera__" (código sol-201500054163-tra)

Participantes en el proyecto:
* Anke Berns: coordinadora del proyecto (Departamento de Filología Francesa e Inglesa)
* Manuel Palomo Duarte: participante en el proyecto (Departamento de Ingeniería Informática)
* José Luis Isla Montes: participante en el proyecto (Departamento de Ingeniería Informática)
* Javier Osuna Herrera: desarrollador (Departamento de Ingeniería Informática)
* Owayss Kabtoul: administrador técnico y desarrollador (Departamento de Ingeniería Informática)


Estudiantes de la Universidad de Cádiz que colaboraron:
* Alicia Garrido Guerrero (Departamento de Filología Francesa e Inglesa)
* Mercedes Paéz Piña (Departamento de Filología Francesa e Inglesa)
* Salvador Reyes Sánchez (Departamento de Filología Francesa e Inglesa)
* Andrea Calderón Márquez (Departamento de Filología Francesa e Inglesa)
* Federico Carrillo Chaves (Escuela Superior de Ingeniería)
