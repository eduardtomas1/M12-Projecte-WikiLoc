CREATE TABLE usuaris
(
	username varchar(25),
	pwd varchar(255),
	mail varchar(255),
	foto varchar(255),
	usuaris_ruta int(4),
	CONSTRAINT pk_username_id PRIMARY KEY (username),
	CONSTRAINT fk_ruta_id (usuaris_ruta) FOREIGN KEY ruta (id_ruta)
)

CREATE TABLE ruta
(
	id_ruta int(4),
	titol varchar(255),
	descripcio varchar(255),
	text varchar(255),
	distancia varchar(255),
	temps varchar(255),
	desn_p varchar(255),
	desn_n varchar(255),
	dificultat varchar(255),
	ruta_usuari varchar(25),
	CONSTRAINT pk_ruta_id PRIMARY KEY (id_ruta),
	CONSTRAINT fk_user_id (ruta_usuari) FOREIGN KEY usuaris (usuari)
)

CREATE TABLE punt
(
    num_id int(4),
	nom varchar(255),
	descripcio varchar(255),
	foto varchar(255),
	lat varchar(255),
	lon varchar(255),
	alt varchar(255),
	punt_ruta int(4),
	punt_tipus int(4),
	CONSTRAINT pk_num_id PRIMARY KEY (num_id, punt_ruta),
	CONSTRAINT fk_ruta_id (punt_ruta) FOREIGN KEY ruta (id_ruta),
	CONSTRAINT fk_tipus_id (punt_tipus) FOREIGN KEY tipus (tipus_id)
)

CREATE TABLE tipus
(
    tipus_id int(4),
	nom varchar(255),
	icona varchar(255),
	CONSTRAINT pk_tipus_id PRIMARY KEY (tipus_id)
)

CREATE TABLE comentari
(
    comentari_id int(4),
	text varchar(255),
	feta varchar(255),
	dificultat varchar(255),
	v_inf int(1),
	v_pai int(1),
	v_seg int(1),
	mt DATE,
	comentari_user varchar(25),
	comentari_ruta int(4)
	CONSTRAINT pk_comentari_id PRIMARY KEY (comentari_id),
	CONSTRAINT fk_user_id (comentari_user) FOREIGN KEY usuaris (username),
	CONSTRAINT fk_ruta_id (comentari_ruta) FOREIGN KEY ruta (id_ruta)
)

CREATE TABLE comentaris_usuaris
(
	comentaris_usuaris_id int(4),
	relacio_username varchar(25),
	relacio_comentari int(4),
	CONSTRAINT pk_relacio_id PRIMARY KEY (comentaris_usuaris_id),
	CONSTRAINT fk_user_id (relacio_username) FOREIGN KEY usuaris (username),
	CONSTRAINT fk_comentari_id (relacio_comentari) FOREIGN KEY comentari (comentari_id)
)

