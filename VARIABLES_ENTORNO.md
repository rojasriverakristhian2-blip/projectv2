# 📋 Guía de Variables de Entorno

Este documento explica cómo se usan las variables de entorno en el proyecto.

## 📁 Ubicación de las Variables de Entorno

### Backend (Spring Boot)
- **Archivo de configuración**: `project-backend/src/main/resources/application.yaml`
- **Variables definidas en**: `docker-compose.yml` o archivo `.env`
- **Configuración CORS**: `project-backend/src/main/java/com/project/gestionconocimiento/project_backend/config/CorsConfig.java`

### Frontend (Angular)
- **Archivos de entorno**: 
  - `src/environments/environment.ts` (desarrollo)
  - `src/environments/environment.prod.ts` (producción)
  - `src/environments/environment.web.ts` (web)
- **Uso**: Los servicios importan `environment.apiUrl` para las peticiones HTTP

### Docker Compose
- **Archivo**: `docker-compose.yml`
- **Archivo de ejemplo**: `ENV.example` (copia a `.env` para usar)

## 🔧 Variables de Entorno Disponibles

### Base de Datos MySQL
```bash
MYSQL_ROOT_PASSWORD=root              # Contraseña del usuario root
MYSQL_DATABASE=gestionconocimiento    # Nombre de la base de datos
DB_USERNAME=admin                     # Usuario de la base de datos
DB_PASSWORD=admin                     # Contraseña del usuario
MYSQL_PORT=3306                       # Puerto de MySQL
```

### Spring Boot Backend
```bash
SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/gestionconocimiento?useSSL=false&serverTimezone=UTC
SPRING_DATASOURCE_USERNAME=admin
SPRING_DATASOURCE_PASSWORD=admin
SPRING_JPA_HIBERNATE_DDL_AUTO=update  # update, create, validate, none
SPRING_JPA_SHOW_SQL=true              # Mostrar queries SQL en consola
```

### CORS Configuration
```bash
CORS_ALLOWED_ORIGINS=http://localhost:4200,http://localhost:80,http://localhost
```
**Nota**: Separa múltiples URLs con comas (sin espacios).

## 🚀 Cómo Usar las Variables de Entorno

### Opción 1: Usar archivo .env (Recomendado)

1. Copia el archivo de ejemplo:
   ```bash
   cp ENV.example .env
   ```

2. Edita el archivo `.env` con tus valores:
   ```bash
   # Edita .env con tus valores personalizados
   DB_PASSWORD=mi_password_seguro
   CORS_ALLOWED_ORIGINS=https://mi-dominio.com,https://www.mi-dominio.com
   ```

3. Docker Compose cargará automáticamente el archivo `.env`:
   ```bash
   docker-compose up -d
   ```

### Opción 2: Variables de entorno del sistema

Exporta las variables antes de ejecutar docker-compose:
```bash
export DB_PASSWORD=mi_password_seguro
export CORS_ALLOWED_ORIGINS=https://mi-dominio.com
docker-compose up -d
```

### Opción 3: Valores por defecto

Si no defines las variables, se usarán los valores por defecto definidos en `docker-compose.yml`:
```yaml
${DB_USERNAME:-admin}  # Usa 'admin' si DB_USERNAME no está definida
```

## 📝 Configuración por Entorno

### Desarrollo Local (sin Docker)
- **Backend**: Usa `application.yaml` con valores por defecto
- **Frontend**: Usa `environment.ts` con `apiUrl: '/api'` o `'http://localhost:8080/api'`

### Desarrollo con Docker
- **Backend**: Variables de entorno desde `docker-compose.yml` o `.env`
- **Frontend**: Usa URLs relativas (`/api`) que nginx proxy al backend

### Producción
1. Crea un archivo `.env.prod` con valores de producción
2. Ajusta `CORS_ALLOWED_ORIGINS` con las URLs de tu frontend desplegado
3. Cambia las contraseñas por valores seguros
4. Ejecuta: `docker-compose --env-file .env.prod up -d`

## 🔍 Verificación

### Verificar que las variables se están usando:

1. **Backend**: Revisa los logs al iniciar:
   ```bash
   docker logs spring-backend
   ```
   Deberías ver la URL de conexión a la base de datos.

2. **Frontend**: Los servicios usan `environment.apiUrl`:
   ```typescript
   // En los servicios (auth.service.ts, etc.)
   private baseUrl = `${environment.apiUrl}/auth`;
   ```

3. **CORS**: Verifica en `CorsConfig.java` que lee la variable:
   ```java
   @Value("${cors.allowed-origins:http://localhost:4200,http://localhost:80}")
   private String allowedOrigins;
   ```

## ⚠️ Consideraciones Importantes

1. **Seguridad**: 
   - Nunca subas el archivo `.env` al repositorio
   - Asegúrate de que `.env` esté en `.gitignore`
   - Usa contraseñas seguras en producción

2. **Formato de Variables**:
   - Spring Boot acepta variables en formato `SPRING_DATASOURCE_URL` o `SPRING.DATASOURCE.URL`
   - En `application.yaml` se usan con sintaxis: `${VARIABLE:valor_por_defecto}`

3. **Frontend**:
   - Angular compila las variables de entorno en tiempo de build
   - Para cambiar la API URL en producción, debes reconstruir la imagen
   - Se usan URLs relativas (`/api`) que funcionan con el proxy de nginx

4. **CORS**:
   - Separa múltiples URLs con comas (sin espacios)
   - Incluye `http://localhost:4200` para desarrollo local
   - En producción, incluye solo las URLs de tu frontend desplegado

## 🐛 Solución de Problemas

### El backend no se conecta a la base de datos
- Verifica que `SPRING_DATASOURCE_URL` apunte al contenedor correcto (`mysql:3306` en Docker)
- Revisa que `DB_USERNAME` y `DB_PASSWORD` coincidan con los de MySQL

### Error de CORS en el frontend
- Verifica que la URL del frontend esté en `CORS_ALLOWED_ORIGINS`
- Asegúrate de incluir el protocolo (`http://` o `https://`)
- Reinicia el contenedor del backend después de cambiar CORS

### El frontend no encuentra la API
- Verifica que nginx esté configurado para hacer proxy de `/api/` al backend
- Revisa que `environment.apiUrl` sea `/api` en los archivos de entorno
- En desarrollo local sin Docker, usa `http://localhost:8080/api`

## 📚 Referencias

- [Spring Boot Externalized Configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config)
- [Docker Compose Environment Variables](https://docs.docker.com/compose/environment-variables/)
- [Angular Environment Configuration](https://angular.io/guide/build#configuring-application-environments)

