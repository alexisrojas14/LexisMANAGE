<!-- Banner -->
<h1 align="center">🏋️‍♂️ Lexis Manage</h1>
<p align="center">
  <i>Sistema de información para gimnasios, academias y negocios con membresías</i>
</p>

---

<!-- Badges -->
<p align="center">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"/>
  <img src="https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white"/>
  <img src="https://img.shields.io/badge/Ubuntu-E95420?style=for-the-badge&logo=ubuntu&logoColor=white"/>
  <img src="https://img.shields.io/badge/NetBeans-1B6AC6?style=for-the-badge&logo=apache-netbeans-ide&logoColor=white"/>
</p>

---

## 🚀 Características principales

- 🔐 **Registro y acceso seguro** de administradores.  
- 👥 CRUD de **usuarios** y **membresías**.  
- 🎟️ Control de acceso según el estado de la membresía.  
- 💳 Gestión de **pagos y renovaciones**.  
- 📊 Reportes de asistencia e ingresos.  
- 👔 Administración de **empleados y roles**.  
- ⚙️ **Personalización** del sistema para cada establecimiento.  

---

## 🖥️ Tecnologías utilizadas

- **Lenguaje:** Java ☕  
- **IDE:** Apache NetBeans  
- **Base de Datos:** MySQL (Ubuntu Server en VirtualBox)  
- **Librerías principales:**  
  - `javax.swing` → Interfaz gráfica  
  - `java.sql` → Conexión a la base de datos  
  - `java.util` → Utilidades generales  

---

## 🏗️ Arquitectura

Lexis Manage sigue una arquitectura **cliente-servidor**:  
🖥️ Cliente en **Java (Swing GUI)** → 📡 Conexión SQL → 🗄️ **Servidor Ubuntu con MySQL**  

---

## 📊 Base de Datos

Principales entidades:  

- 👤 `Usuario` → Datos de clientes  
- 🏷️ `Membresia` → Tipos y duración de membresías  
- 💵 `Pago` → Control de pagos y vencimientos  
- 🛂 `Acceso` → Registro de entradas  
- 👔 `Empleados` → Gestión de personal  
- 🏢 `Personalizacion_Establecimiento` → Datos de cada gimnasio/academia  

---

## 🎨 Paleta de Diseño

El sistema usa un **estilo minimalista y profesional**:  

| Color | Hex | Uso |
|-------|------|-----|
| 🔴 Rojo | `#ff4d58` | Alertas y acciones importantes |
| ⚪ Blanco | `#ffffff` | Textos y fondos limpios |
| 🌫️ Gris Claro | `#abbdc9` | Elementos secundarios |
| 🔵 Azul Oscuro | `#1a2737` | Fondos principales |
| ⚫ Negro | `#0a101e` | Contraste y elegancia |

---

## 📸 Mockups / Vistas

- 🔑 Login & Registro  
- 👥 Gestión de Usuarios  
- 🏷️ Membresías  
- 💳 Pagos  
- 📊 Estadísticas  
- 🛂 Control de Acceso  
- 👔 Empleados  
- ⚙️ Configuración  

---

## ⚡ Instalación y ejecución

1. Clonar el repositorio  
   ```bash
   git clone https://github.com/usuario/LexisManage.git
   cd LexisManage

2.Importar la base de datos:
```bash
   mysql -u username -p database_name < database/backup_original.sql

3.Configurar conexión a BD:
    Editar el archivo de configuración en src/ con las credenciales de tu base de datos.

4.Compilar y ejecutar:
    # Usando Ant
    ant build
    ant run
  # O ejecutar el JAR directamente
    java -jar LexisManage.jar  


##🔧 Estructura del Proyecto
LexisMANAGE/
├── src/                 # Código fuente Java
├── test/               # Pruebas unitarias
├── database/           # Scripts de base de datos
│   └── backup_original.sql
├── docs/               # Documentación
│   ├── IEEE_LexisMANAGE.pdf
│   └── prueba_reporte.pdf
├── build.xml           # Configuración Ant
├── manifest.mf         # Manifest para JAR
└── README.md           # Este archivo

##📄 Licencia
Este proyecto está bajo la Licencia MIT. Ver el archivo LICENSE para más detalles.

##👨‍💻 Autor
Cristian Alexis Rojas Herreño

Email: alexis.rojas.soft@gmail.com

GitHub: @alexisrojas14

Politécnico Internacional

##🙏 Agradecimientos
-Politécnico Internacional por el apoyo académico

-Comunidad de Java por las herramientas y recursos

Documentación desarrollada en Septiembre de 2024

