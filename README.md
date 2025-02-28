# Instrucciones para Ejecutar el Proyecto

## Clonar el Repositorio
Para descargar el proyecto, ejecute el siguiente comando en su terminal:

```bash
git clone git@github.com:MiguelParra4573/sofka-bank.git
```

## Navegar a la Carpeta del Proyecto
Una vez clonado el repositorio, acceda a la carpeta del proyecto con el siguiente comando:

```bash
cd sofka-bank
```

## Levantar el Proyecto con Docker Compose
El proyecto está configurado para ejecutarse utilizando Docker Compose. Para construir y levantar los servicios, ejecute el siguiente comando:

```bash
docker-compose up --build
```

Este comando descargará las dependencias necesarias, construirá las imágenes y levantará los contenedores requeridos para el funcionamiento del proyecto.

## Persistencia de Datos
El proyecto utiliza Hibernate para la persistencia de datos. No es necesario adjuntar ni ejecutar ningún archivo `.sql`, ya que Hibernate se encargará automáticamente de crear y gestionar la base de datos en tiempo de ejecución.
```
Este formato en Markdown debería ser más claro y fácil de seguir para los usuarios del repositorio.
```
## Postman

Para poder realizar pruebas del los end points, en el repositorio encontrara el archivo:
``` 
BANK-SOFKA - MIGUEL PARRA.postman_collection.json 
```
el mismo debe de importar en Postman y poder visualizar cada end point creado con sus parametros y body's a consideración

