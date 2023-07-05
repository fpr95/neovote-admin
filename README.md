# neovote-admin

neovote es un sistema de votación separado en 2 partes, una para administración() y otra para los votantes. Los administradores son los encargados de 
crear/editar/activar/suspender/eliminar los **procesos de elección** además de poder agregar/editar/remover las **propuestas** para cada uno de éstos. 
Aún hay implementaciones pendientes para la parte de administración como las secciones 'Padrón Electoral' y 'Estadísticas'.

## Stack

**Java 17**, **Spring Boot**, **Spring Security**, **Spring Data JPA** con **Hibernate**, **MySQL** como RDBMS y **Thymeleaf**.

## Algunas funciones

Algunas funciones implementadas son:
- Registro de usuarios con envío de correo para la verificación del mismo. Los campos 'correo' y 'nombre de usuario' no se pueden repetir.
- Recuperación de contraseña con enlace enviado al correo registrado.
- Inicio sesión usuario (administrador)
- Creación de procesos de elección y edición de los campos ingresados
- A las elecciones creadas se les puede agregar (y editar) propuestas
- Las elecciones creadas aparecen por defecto suspendidas con el fin de poder agregar las propuestas y/o editar campos antes de que sean visibles a los votantes,
  se pueden activar (o volver a suspender) antes de la fecha y hora de inicio del proceso
- Calendario incorporado para ayudar a los adminstradores a mantener el orden

## Vista Previa

Algunas de las vistas ya implementadas incluyen la de registro e inicio de sesión
![login](https://user-images.githubusercontent.com/102566410/228316409-45b6214d-4413-49b1-ba32-7e17d5e0f891.png)
![register-2](https://user-images.githubusercontent.com/102566410/228316630-a3bcf3c8-0041-4800-9c1c-fa66a0f57a8e.png)
##
Agregar un nuevo proceso electoral
![new-election1](https://user-images.githubusercontent.com/102566410/228316852-2208de54-5bc6-41a6-ba8a-3c9ed34f77d1.png)
##
Ver todos los procesos creados presentes en el sistema
![elections-list-3](https://user-images.githubusercontent.com/102566410/228320170-ae370c56-7604-4fe7-bc31-9da2656d7011.png)
##
Editar los campos de un proceso creado
![edit-election-1](https://user-images.githubusercontent.com/102566410/228317382-6a0008d0-b4c7-4946-981d-dc6e5da0d251.png)
##
Formulario para agregar propuestas
![new-proposal-modal](https://user-images.githubusercontent.com/102566410/228317518-33be5b14-533b-420c-ade8-05c733970340.png)
##
