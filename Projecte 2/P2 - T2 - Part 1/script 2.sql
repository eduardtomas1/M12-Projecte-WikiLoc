-- grant CREATE ANY DIRECTORY to alumne;
CREATE OR REPLACE DIRECTORY csv_dir AS '/Documentos/vehicles.csv';

CREATE TABLE ext_vehicles_modelo (
  modelo VARCHAR2(255)
)
ORGANIZATION EXTERNAL (
  TYPE oracle_loader
  DEFAULT DIRECTORY csv_dir
  ACCESS PARAMETERS (
    RECORDS DELIMITED BY NEWLINE
    FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"' 
    MISSING FIELD VALUES ARE NULL
    (modelo CHAR(255))
  )
  LOCATION ('vehicles.csv')
)
REJECT LIMIT UNLIMITED;

INSERT INTO Marca_model (marca_model)
SELECT DISTINCT modelo FROM ext_vehicles_modelo;

DROP TABLE ext_vehicles_modelo;
-- DROP DIRECTORY csv_dir; -- Uncomment if you want to remove the directory as well
