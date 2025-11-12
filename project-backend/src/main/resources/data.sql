-- Usar la base de datos correcta
USE gestionconocimiento;

-- Insertar usuarios (usar IGNORE para evitar duplicados)
INSERT IGNORE INTO usuario (cedula, nombre, correo, contrasena, fecha_nacimiento, telefono, rol)
VALUES 
(123456789, 'Kristhiam Rojas', 'kristhiam@example.com', 'admin123', '2000-05-10', '3124567890', 'presidente'),
(987654321, 'Laura Gómez', 'laura@example.com', 'residente123', '1998-09-22', '3109876543', 'residente'),
(1002345678, 'Andrés Ramírez', 'andres.ramirez@example.com', 'residente123', '1995-03-12', '3104567890', 'residente'),
(1003456789, 'Camila Torres', 'camila.torres@example.com', 'residente123', '1999-07-21', '3209876543', 'residente'),
(1004567890, 'Sebastián López', 'sebastian.lopez@example.com', 'residente123', '1992-01-09', '3116547890', 'residente'),
(1005678901, 'Valentina Ruiz', 'valentina.ruiz@example.com', 'residente123', '2000-11-05', '3127894560', 'residente'),
(1006789012, 'Carlos Martínez', 'carlos.martinez@example.com', 'residente123', '1988-04-18', '3003456789', 'residente'),
(1007890123, 'Mariana Herrera', 'mariana.herrera@example.com', 'residente123', '1996-08-27', '3019876540', 'residente'),
(1008901234, 'Felipe Castro', 'felipe.castro@example.com', 'residente123', '1994-06-10', '3026549870', 'residente'),
(1009012345, 'Daniela Gómez', 'daniela.gomez@example.com', 'residente123', '1997-09-30', '3037654321', 'residente'),
(1010123456, 'Juan Esteban Rojas', 'juan.rojas@example.com', 'residente123', '1993-12-15', '3048765432', 'residente'),
(1011234567, 'Natalia Sánchez', 'natalia.sanchez@example.com', 'residente123', '2001-02-03', '3059988776', 'residente');