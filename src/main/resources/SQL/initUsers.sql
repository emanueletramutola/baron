DO
$body$
BEGIN
   IF EXISTS (
      SELECT
      FROM   pg_catalog.pg_user
      WHERE  usename = 'baron') THEN
		DROP OWNED BY baron;
		DROP USER baron;
   END IF;
END
$body$;

CREATE USER baron WITH NOSUPERUSER NOCREATEDB NOCREATEROLE ENCRYPTED PASSWORD 'aPCVqV9BGfLVW6Xf';

DO
$body$
    BEGIN
        IF EXISTS (
                SELECT
                FROM   pg_catalog.pg_user
                WHERE  usename = 'baron_uscrn') THEN
            DROP OWNED BY baron_uscrn CASCADE;
            DROP USER baron_uscrn;
        END IF;
    END
$body$;

CREATE USER baron_uscrn WITH NOSUPERUSER NOCREATEDB NOCREATEROLE ENCRYPTED PASSWORD 'YVvy38T87xsxVDxS';
