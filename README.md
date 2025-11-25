# 🎃 TrucoOTrato

TrucoOTrato es un proyecto que combina creatividad, lógica y diversión. Su objetivo principal es ofrecer una base sólida para el desarrollo de aplicaciones interactivas centradas en la gestión, automatización o gamificación de “trucos” y “tratos” entre usuarios o elementos del sistema.

---

## 🧩 Contenido del repositorio

- **Código fuente principal** en `src/` (Organizado en paquetes `Interface` y `Roulette`).
- **Gestor de dependencias Maven** (`pom.xml`, `mvnw`, `mvnw.cmd`, `.mvn/`).
- **Recursos** (CSS, imágenes, vistas, scripts) en `src/main/resources/`.
- **Pruebas unitarias** en `src/test/java/`.
- **Documentación adicional** (este README).

---

## 🎯 Funcionalidades y Clases principales

El proyecto se estructura en paquetes funcionales, enfocados en la Interfaz de Usuario y la lógica de la Ruleta.

### Paquete `Roulette`
Contiene la lógica y la representación visual de la ruleta del juego.
- **`RouletteApp.java`**: Punto de entrada o control principal del módulo de la ruleta.
- **`WheelView.java`**: Clase encargada de la visualización (vista) de la ruleta, gestionando su despliegue y posiblemente la animación de giro.

### Paquete `Interface`
Contiene las clases principales de la Interfaz de Usuario, incluyendo el punto de entrada de la aplicación y controladores.
- **`Launcher.java`**: La clase principal utilizada para iniciar la aplicación (común en aplicaciones JavaFX).
- **`HelloApplication.java`**: La clase de aplicación base para la UI.
- **`ControladorFormulario.java`**: Clase que gestiona la interacción del usuario con formularios o elementos específicos de la UI.

---

## 📸 Capturas de pantalla

### Ruleta
![Ruleta](Rouleta.png)  
> Captura de la ruleta.

### Inicio de sesion
![Inicio Sesion](InicioSesion.png)  
> captura del inicio de sesión

---

## 🧰 Tecnologías utilizadas

| Tecnología           | Propósito                                             |
|----------------------|-------------------------------------------------------|
| Java                 | Lenguaje de programación principal                    |
| Maven                | Gestión del proyecto y dependencias                   |
| CSS                  | Diseño visual de la interfaz                          |
| Git & GitHub         | Control de versiones y colaboración                   |
| JavaFX / Swing / Spring Boot | Interfaz de usuario y backend (según implementación) |

---

## 🚀 Cómo clonar y ejecutar el proyecto

```bash
git clone [https://github.com/Ivannovichh/TrucoOTrato.git](https://github.com/Ivannovichh/TrucoOTrato.git)
cd TrucoOTrato
---

## 📁 Estructura del proyecto

```

TrucoOTrato/
├── docs/
│   ├── ruleta.png
│   └── carta.png
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── Interface/          # ControladorFormulario.java, HelloApplication.java, Launcher.java
│   │   │   └── Roulette/           # RouletteApp.java, WheelView.java
│   │   └── resources/  # CSS, imágenes, vistas, scripts
│   └── test/
│       └── java/       # Pruebas unitarias
├── pom.xml
├── mvnw / mvnw.cmd
├── .mvn/
└── .gitignore

```

---

## 💬 Autor

Iván Sánchez Juárez  
GitHub: [@Ivannovichh](https://github.com/Ivannovichh)  
Proyecto creado con 💻, 🎃 y ☕
