Archivos para enviar y recibir datos en el login



**Frontend (angular) puerto 4200:**

 	auth.service.ts (Encargado de enviar la solicitud HTTP al backend.)

 	login.ts (Controlador del componente que maneja la lógica y los mensajes.)

 	login.html (Interfaz del login)

 	login.css (Simple estilo visual.)



**Backend (Spring boot) puerto 8080:**

 	AuthController.java (se encarga de recibir las solicitudes HTTP que llegan desde el frontend (Angular), procesarlas y devolver una respuesta)

 	AuthService.java (contiene la lógica principal del login y registro)

 	UsuarioRepository (es el que se comunica directamente con la base de datos.)

 



\[Frontend Angular]

   ↓ (envía correo y contraseña)

\[AuthController]

   ↓ (recibe y pasa la solicitud)

\[AuthService]

   ↓ (valida usuario en BD)

\[UsuarioRepository]

   ↓ (busca el usuario)

\[AuthService]

   ↓ (confirma o rechaza)

\[AuthController]

   ↓ (responde "Login exitoso" o "Login fallido")

\[Frontend Angular]

   → Muestra mensaje al usuario





**COMANDOS BACKEND:**

mvn spring-boot:run	(Ejecuta el proyecto, Para iniciar el backend normalmente)


 	mvn clean spring-boot:run	(Limpia + recompila + ejecuta, Si hay errores raros o cambios grandes)



**COMANDOS FRONTEND:**

 	sg serve (ejecuta angular)



npm run start:full (ejecuta los dos)


**OWL (actividad)**
Presidente
Crea una actividad y la envía
Se guarda en el archivo .ttl (con OWLAPI en Java)

Backend
Usa OWLAPI para manipular la ontología
Añade instancias (individuos) de tipo “Actividad”

Residente
Consulta las actividades almacenadas
Lee los datos desde el archivo .ttl

**tareas completadas**
- el presidente crea una actividad
- el presidente elimina una actividad
- el presidente busca una actividad
- el presidente edita una actividad
- el presidente visualiza una actividad


-el residente observa la actividad


**docker**
# Parar y eliminar contenedores anteriores (limpieza)
docker compose down

# Reconstruir TODO desde cero
docker compose up --build

# Opción detallada:
docker compose down
docker compose build --no-cache  # Forzar reconstrucción completa
docker compose up

# Levantar todos los servicios
docker compose up -d

# Verificar que todos estén corriendo
docker compose ps

# Si el backend no inicia, ver logs específicos
docker compose logs backend

# O forzar reinicio
docker compose restart backend