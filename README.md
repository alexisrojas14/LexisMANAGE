<!-- Banner -->
<h1 align="center">🏋️‍♂️ Lexis Manage</h1>
<h3 align="center">Sistema de información para gimnasios, academias y negocios con membresías</h3>

---

<!-- Tech Logos -->
<p align="center">
  <a href="https://www.java.com/" target="_blank">
    <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" alt="Java" width="60" height="60"/>
  </a>
  <a href="https://www.mysql.com/" target="_blank">
    <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/mysql/mysql-original.svg" alt="MySQL" width="60" height="60"/>
  </a>
  <a href="https://ubuntu.com/" target="_blank">
    <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/ubuntu/ubuntu-plain.svg" alt="Ubuntu" width="60" height="60"/>
  </a>
  <a href="https://netbeans.apache.org/" target="_blank">
    <img src="https://upload.wikimedia.org/wikipedia/commons/9/98/Apache_NetBeans_Logo.svg" alt="NetBeans" width="60" height="60"/>
  </a>
</p>


---

## <h2 align="center">🚀 Características principales</h2>


- 🔐 **Registro y acceso seguro** de administradores.  
- 👥 CRUD de **usuarios** y **membresías**.  
- 🎟️ Control de acceso según el estado de la membresía.  
- 💳 Gestión de **pagos y renovaciones**.  
- 📊 Reportes de asistencia e ingresos.  
- 👔 Administración de **empleados y roles**.  
- ⚙️ **Personalización** del sistema para cada establecimiento.  

---

## <h2 align="center">🖥️ Tecnologías utilizadas</h2>


- **Lenguaje:** Java ☕  
- **IDE:** Apache NetBeans  
- **Base de Datos:** MySQL (Ubuntu Server en VirtualBox)  
- **Librerías principales:**  
  - `javax.swing` → Interfaz gráfica  
  - `java.sql` → Conexión a la base de datos  
  - `java.util` → Utilidades generales  

---

## <h2 align="center">🏗️ Arquitectura</h2>

Lexis Manage sigue una arquitectura **cliente-servidor**:  
🖥️ Cliente en **Java (Swing GUI)** → 📡 Conexión SQL → 🗄️ **Servidor Ubuntu con MySQL**  

---

## <h2 align="center">📊 Base de Datos</h2>

Principales entidades:  

- 👤 `Usuario` → Datos de clientes  
- 🏷️ `Membresia` → Tipos y duración de membresías  
- 💵 `Pago` → Control de pagos y vencimientos  
- 🛂 `Acceso` → Registro de entradas  
- 👔 `Empleados` → Gestión de personal  
- 🏢 `Personalizacion_Establecimiento` → Datos de cada gimnasio/academia  

---

## <h2 align="center">🎨 Paleta de Diseño</h2>

El sistema usa un **estilo minimalista y profesional**:  

| Color | Hex | Uso |
|-------|------|-----|
| 🔴 ![#ff4d58](https://placehold.co/15x15/ff4d58/ff4d58.png) Rojo | `#ff4d58` | Alertas y acciones importantes |
| ⚪ ![#ffffff](https://placehold.co/15x15/ffffff/ffffff.png) Blanco | `#ffffff` | Textos y fondos limpios |
| 🌫️ ![#abbdc9](https://placehold.co/15x15/abbdc9/abbdc9.png) Gris Claro | `#abbdc9` | Elementos secundarios |
| 🔵 ![#1a2737](https://placehold.co/15x15/1a2737/1a2737.png) Azul Oscuro | `#1a2737` | Fondos principales |
| ⚫ ![#0a101e](https://placehold.co/15x15/0a101e/0a101e.png) Negro | `#0a101e` | Contraste y elegancia |


---

## <h2 align="center">📸 Mockups / Vistas</h2>

<div align="center">

| Vista | Descripción |
|-------|-------------|
| 🔑 | Login & Registro |
| 👥 | Gestión de Usuarios |
| 🏷️ | Membresías |
| 💳 | Pagos |
| 📊 | Estadísticas |
| 🛂 | Control de Acceso |
| 👔 | Empleados |
| ⚙️ | Configuración |

</div>


---

## <h2 align="center">⚡ Instalación y ejecución</h2>

1. Clonar el repositorio  
   ```bash
   git clone https://github.com/usuario/LexisManage.git
   cd LexisManage

2.Importar la base de datos:
```bash
   mysql -u username -p database_name < database/backup_original.sql
```

3.Configurar conexión a BD:

    Editar el archivo de configuración en src/ con las credenciales de tu base de datos.

4.Compilar y ejecutar:

    # Usando Ant
    ant build
    ant run
    
    # O ejecutar el JAR directamente
    java -jar LexisManage.jar  

---

## <h2 align="center">🔧 Estructura del Proyecto</h2>
  ```bash
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
```

---
## <h2 align="center">📄 Licencia</h2>

<p align="center">
  <a href="LICENSE">
    <img src="https://img.shields.io/badge/MIT-License-blueviolet?style=for-the-badge&logo=open-source-initiative&logoColor=white"/>
  </a>
</p>




---

##   <h2 align="center">👨‍💻 Autor</h2>

<p align="center">
  <img src="https://avatars.githubusercontent.com/u/000000?v=4" width="120" height="120" style="border-radius: 50%;" alt="Foto de perfil"/>  
</p>

<p align="center">
  <b>Alexis Rojas</b>  
</p>

<p align="center">
  📧 <a href="mailto:alexis.rojas.soft@gmail.com">alexis.rojas.soft@gmail.com</a>  
  <br>
  🐙 <a href="https://github.com/alexisrojas14">@alexisrojas14</a>  
  <br>
  🎓 Politécnico Internacional  
</p>
<p align="center">
  <a href="mailto:alexis.rojas.soft@gmail.com">
    <img src="https://img.shields.io/badge/Email-D14836?style=for-the-badge&logo=gmail&logoColor=white"/>
  </a>
  <a href="https://github.com/alexisrojas14">
    <img src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white"/>
  </a>
</p>


---

##  <h2 align="center">🙏 Agradecimientos </h2>


<p align="center">
  📌 Gracias al <b>Politécnico Internacional</b> por el apoyo académico  
  <br>
  📌 A la <b>Comunidad de Java</b> por las herramientas y recursos  
</p>

---

📘 <i>Documentación desarrollada en Septiembre de 2024</i>  


