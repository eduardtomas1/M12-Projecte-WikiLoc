CREATE TABLE usuari
(
	login varchar(25),
	pwd varchar(255),
	mail varchar(255),
	foto varchar(255),
	CONSTRAINT pk_ruta_id PRIMARY KEY (login)
)

CREATE TABLE ruta
(
	id int,
	titol varchar(255),
	descripcio varchar(255),
	text varchar(255),
	distancia varchar(255),
	temps varchar(255),
	desn_p varchar(255),
	desn_n varchar(255),
	dificultat varchar(255),
	CONSTRAINT pk_ruta_id PRIMARY KEY (id)
)

CREATE TABLE punt
(
    
)

CREATE TABLE tipus
(
    
)

CREATE TABLE comentari
(
    
)