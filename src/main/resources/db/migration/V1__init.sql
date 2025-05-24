CREATE TABLE usuarios (
    id VARCHAR(50) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(100) NOT NULL,
    rol VARCHAR(20) NOT NULL
);

CREATE TABLE votaciones (
    id VARCHAR(50) PRIMARY KEY,
    titulo VARCHAR(200) NOT NULL,
    descripcion TEXT NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    estado VARCHAR(20) NOT NULL,
    admin_id VARCHAR(50) REFERENCES usuarios(id)
);

CREATE TABLE opciones (
    id SERIAL PRIMARY KEY,
    votacion_id VARCHAR(50) REFERENCES votaciones(id),
    descripcion VARCHAR(200) NOT NULL
);

CREATE TABLE votos (
    id SERIAL PRIMARY KEY,
    opcion_id INT REFERENCES opciones(id),
    usuario_id VARCHAR(50) REFERENCES usuarios(id),
    fecha_voto DATE NOT NULL
);
