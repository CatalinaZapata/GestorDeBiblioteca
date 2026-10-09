# 🧠 Sumativa Semana 9 - Desarrollo Orientado a Objetos II

---

## 👤 Autor del proyecto
- **Nombre completo:**  Catalina Zapata
- **Carrera:** Analista Programador
- **Nombre del Proyecto:** GestorDeBiblioteca
---
## 📘 Descripción general del proyecto
Sistema de gestión para una biblioteca escolar, desarrollado en Java con Swing y MySQL (JDBC).

Permite gestionar usuarios, estudiantes, libros, categorías, préstamos y devoluciones, incorporando operaciones CRUD (crear, listar, actualizar y eliminar), autenticación de usuarios con roles y generación de reportes.

El proyecto aplica principios de programación orientada a objetos y los patrones de diseño MVC, DAO y Singleton, además de contemplar concurrencia y sincronización para el registro de préstamos y el control del stock de libros.

---
## 🧱 Estructura general del proyecto

```plaintext
```text
GestorDeBiblioteca/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── org.example/
│       │       │
│       │       ├── app/
│       │       │   └── Main.java                       # Punto de entrada y ejecución principal del sistema
│       │       │
│       │       ├── controller/
│       │       │   ├── ControladorAutenticacion.java   # Gestiona la autenticación y validación de credenciales
│       │       │   ├── ControladorEstudiante.java      # Coordina las operaciones de gestión de estudiantes
│       │       │   ├── ControladorLibro.java           # Coordina las operaciones de gestión de libros
│       │       │   ├── ControladorPrestamo.java        # Coordina los préstamos y las devoluciones de libros
│       │       │   └── ControladorReporte.java         # Coordina la generación y consulta de reportes
│       │       │
│       │       ├── dao/
│       │       │   ├── CategoriaDAO.java               # Gestiona las operaciones de acceso a datos de categorías
│       │       │   ├── CrudDAO.java                    # Define el contrato común para las operaciones CRUD
│       │       │   ├── DatabaseConnection.java         # Gestiona la conexión a MySQL mediante JDBC y Singleton
│       │       │   ├── EstudianteDao.java              # Gestiona los registros de estudiantes en la base de datos
│       │       │   ├── LibroDao.java                   # Gestiona los registros de libros y sus categorías
│       │       │   ├── PrestamoDAO.java                # Gestiona los registros de préstamos y devoluciones
│       │       │   └── UsuarioDao.java                 # Gestiona los usuarios y sus consultas de autenticación
│       │       │
│       │       ├── model/
│       │       │   ├── Categoria.java                  # Representa las categorías de los libros
│       │       │   ├── Estudiante.java                 # Representa a los estudiantes y hereda de Usuario
│       │       │   ├── Libro.java                      # Representa los libros y su información bibliográfica
│       │       │   ├── Prestamo.java                   # Representa los préstamos, fechas y estado de devolución
│       │       │   ├── Rol.java                        # Define los roles disponibles mediante una enumeración
│       │       │   └── Usuario.java                    # Representa a los usuarios, sus credenciales y su rol
│       │       │
│       │       └── view/
│       │           ├── UtilidadesVista.java            # Contiene utilidades reutilizables para las interfaces
│       │           ├── VentanaAutenticacion.java       # Presenta la interfaz de inicio de sesión
│       │           ├── VentanaGestEstudiantes.java     # Interfaz para gestionar los datos de estudiantes
│       │           ├── VentanaGestLibros.java          # Interfaz para administrar libros y categorías
│       │           ├── VentanaMenu.java                # Presenta el menú principal y la navegación del sistema
│       │           ├── VentanaPrestamos.java           # Interfaz para gestionar préstamos y devoluciones
│       │           └── VentanaReportes.java            # Interfaz para visualizar los reportes del sistema
│       │
│       └── resources/
│
├── src/test/
│
├── pom.xml                                             # Configura Maven y las dependencias necesarias
│
└── README.md                                           # Documentación general, funcionalidades y estructura del proyecto
```
```

---
## ⚙️ Instrucciones para compilar y ejecutar `Main`
1. Abrir el proyecto en IntelliJ IDEA.
2. Esperar a que IntelliJ cargue las dependencias del proyecto.
3. Navegar hasta la clase `Main` ubicada en el paquete `app`.
4. Ejecutar el método `main()` presionando el botón **Run** (▶).
5. Verificar los resultados en la consola de ejecución.

---

**Repositorio GitHub:** https: https://github.com/CatalinaZapata/Empresa_Reparto_SpeedFast2.git |
**Fecha de entrega:** 11/10/2026

---

© Duoc UC | Escuela de Informática y Telecomunicaciones | Semana 9