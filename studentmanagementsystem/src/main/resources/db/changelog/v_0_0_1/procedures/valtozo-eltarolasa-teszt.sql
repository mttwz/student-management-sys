--liquibase formatted sql
--changeset splitStatements:true

CREATE PROCEDURE `valtozoEltarolasaTeszt` ()  BEGIN
call createStudent("fn","ln","ph","1999.11.11",@asd);
SELECT @asd;
END $$