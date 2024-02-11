CREATE TABLE Client
(
  client_id VARCHAR(25) PRIMARY KEY,
  nom VARCHAR(25) NOT NULL,
  telefon VARCHAR(9) NOT NULL,
  NIF VARCHAR(9) NOT NULL
);

CREATE TABLE Vehicle
(
  vehicle_id VARCHAR(25) PRIMARY KEY,
  num_llicencia VARCHAR(7) NOT NULL,
  marca_model VARCHAR(255) NOT NULL,
  kilometers INT NOT NULL,
  client_id VARCHAR(25) NOT NULL,
  FOREIGN KEY (client_id) REFERENCES Client(client_id)
);

CREATE TABLE Mecanic
(
  mechanic_id VARCHAR(25) PRIMARY KEY,
  name VARCHAR(25) NOT NULL,
  especialitat VARCHAR(25) NOT NULL
);

CREATE TABLE Reparacio
(
  reparacio_id VARCHAR(25) PRIMARY KEY,
  descripcio VARCHAR(255) NOT NULL,
  vehicle_id VARCHAR(25) NOT NULL,
  data_entrada DATE NOT NULL,
  status_reparacio VARCHAR(25) NOT NULL,
  hores_dedicades INT NOT NULL,
  FOREIGN KEY (vehicle_id) REFERENCES Vehicle(vehicle_id)
);

CREATE TABLE Reparacio_Mecanic
(
  reparacio_mecanic_id VARCHAR(25) PRIMARY KEY,
  reparacio_id VARCHAR(25) NOT NULL,
  mechanic_id VARCHAR(25) NOT NULL,
  FOREIGN KEY (reparacio_id) REFERENCES Reparacio(reparacio_id),
  FOREIGN KEY (mechanic_id) REFERENCES Mecanic(mechanic_id)
);

CREATE TABLE Linies
(
  linies_id VARCHAR(25) PRIMARY KEY,
  reparacio_id VARCHAR(25) NOT NULL,
  tipus_linia VARCHAR(50) NOT NULL,
  FOREIGN KEY (reparacio_id) REFERENCES Reparacio(reparacio_id)
);

CREATE TABLE Linies_Feines_Realitzades
(
  linies_id VARCHAR(25) NOT NULL,
  hores INT NOT NULL,
  PRIMARY KEY (linies_id),
  FOREIGN KEY (linies_id) REFERENCES Linies(linies_id)
);

CREATE TABLE Linies_Peces_Recanvi
(
  linies_id VARCHAR(25) NOT NULL,
  descripcio_peca VARCHAR(255) NOT NULL,
  codi_fabricant VARCHAR(50) NOT NULL,
  preu_unitari DECIMAL(10,2) NOT NULL,
  unitats_usades INT NOT NULL,
  PRIMARY KEY (linies_id),
  FOREIGN KEY (linies_id) REFERENCES Linies(linies_id)
);

CREATE TABLE Linies_Packs
(
  linies_id VARCHAR(25) NOT NULL,
  descripcio_pack VARCHAR(255) NOT NULL,
  preu_fix DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (linies_id),
  FOREIGN KEY (linies_id) REFERENCES Linies(linies_id)
);

CREATE TABLE Altres_Conceptes
(
  linies_id VARCHAR(25) NOT NULL,
  descripcio_concepte VARCHAR(255) NOT NULL,
  preu DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (linies_id),
  FOREIGN KEY (linies_id) REFERENCES Linies(linies_id)
);

CREATE TABLE Factura
(
  factura_id VARCHAR(25) PRIMARY KEY,
  reparacio_id VARCHAR(25) NOT NULL,
  data_generacio DATE NOT NULL,
  num_factura VARCHAR(50) NOT NULL,
  base_imposable DECIMAL(10,2) NOT NULL,
  tipus_IVA VARCHAR(25) NOT NULL,
  cost_ma_obra DECIMAL(10,2) NOT NULL,
  total_amb_IVA DECIMAL(10,2) NOT NULL,
  estat_pagament VARCHAR(25) NOT NULL,
  FOREIGN KEY (reparacio_id) REFERENCES Reparacio(reparacio_id)
);

CREATE TABLE Descomptes
(
  descompte_id VARCHAR(25) PRIMARY KEY,
  linies_id VARCHAR(25) NOT NULL,
  descripcio VARCHAR(255) NOT NULL,
  quantitat_descompte DECIMAL(10,2) NOT NULL,
  FOREIGN KEY (linies_id) REFERENCES Linies(linies_id)
);

--------------------------------------------------------------------------------

ALTER TABLE Client
ADD CONSTRAINT chk_client_telefon CHECK (LENGTH(telefon) = 9),
ADD CONSTRAINT chk_client_nif CHECK (LENGTH(NIF) = 9);

ALTER TABLE Vehicle
ADD CONSTRAINT chk_vehicle_num_llicencia CHECK (LENGTH(num_llicencia) <= 7);

ALTER TABLE Factura
ADD CONSTRAINT chk_factura_base_imposable CHECK (base_imposable >= 0),
ADD CONSTRAINT chk_factura_cost_ma_obra CHECK (cost_ma_obra >= 0),
ADD CONSTRAINT chk_factura_total_amb_IVA CHECK (total_amb_IVA >= 0);

/*CREATE TRIGGER prevent_delete_reparacio
BEFORE DELETE ON Reparacio FOR EACH ROW
BEGIN
    DECLARE cnt INT;
    SELECT COUNT(*) INTO cnt FROM Factura WHERE reparacio_id = OLD.reparacio_id;
    IF cnt > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'No es pot eliminar una reparació amb una factura associada';
    END IF;
END;*/

--------------------------------------------------------------------------------

CREATE INDEX idx_client_nif ON Client(NIF);
CREATE INDEX idx_vehicle_client_id ON Vehicle(client_id);
CREATE INDEX idx_reparacio_vehicle_id ON Reparacio(vehicle_id);
CREATE INDEX idx_reparacio_mecanic_reparacio_id ON Reparacio_Mecanic(reparacio_id);
CREATE INDEX idx_reparacio_mecanic_mechanic_id ON Reparacio_Mecanic(mechanic_id);
CREATE INDEX idx_linies_reparacio_id ON Linies(reparacio_id);
CREATE INDEX idx_factura_reparacio_id ON Factura(reparacio_id);
CREATE INDEX idx_descomptes_linies_id ON Descomptes(linies_id);

--------------------------------------------------------------------------------

INSERT INTO Client (client_id, nom, telefon, NIF) VALUES 
('1', 'Joan Martínez', '612345678', '12345678A'),
('2', 'Anna Soler', '655432198', '98765432B'),
('3', 'Marc López', '689012345', '54321098C'),
('4', 'Laura Torres', '666555444', '87654321D'),
('5', 'Carles Gómez', '633221100', '13579246E'),
('6', 'Eva Roca', '677889900', '24681357F'),
('7', 'Pau Costa', '644332211', '36925814G'),
('8', 'Marta Puig', '699887766', '58246137H'),
('9', 'Alex Sánchez', '688999000', '71428536I'),
('10', 'Laia Fernández', '611223344', '93716485J');

