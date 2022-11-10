-- liquibase formatted sql

-- changeset avodv:1668097141214-1
CREATE TABLE "public"."department" ("id" VARCHAR(255) NOT NULL, "created_at" TIMESTAMP WITHOUT TIME ZONE NOT NULL, "updated_at" TIMESTAMP WITHOUT TIME ZONE NOT NULL, "name" VARCHAR(255), "created_by" VARCHAR(255), "updated_by" VARCHAR(255), "manager_id" VARCHAR(255), CONSTRAINT "department_pkey" PRIMARY KEY ("id"));

-- changeset avodv:1668097141214-2
CREATE TABLE "public"."employees" ("id" VARCHAR(255) NOT NULL, "created_at" TIMESTAMP WITHOUT TIME ZONE NOT NULL, "updated_at" TIMESTAMP WITHOUT TIME ZONE NOT NULL, "email" VARCHAR(255), "full_name" VARCHAR(255), "password" VARCHAR(255), "created_by" VARCHAR(255), "updated_by" VARCHAR(255), "dep_id" VARCHAR(255), CONSTRAINT "employees_pkey" PRIMARY KEY ("id"));

-- changeset avodv:1668097141214-3
ALTER TABLE "public"."department" ADD CONSTRAINT "fk4ibsp65tk9ue7amjgqe2oxmdn" FOREIGN KEY ("created_by") REFERENCES "public"."employees" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset avodv:1668097141214-4
ALTER TABLE "public"."employees" ADD CONSTRAINT "fk4xxve990vv5nd2qbdj4i6m21h" FOREIGN KEY ("created_by") REFERENCES "public"."employees" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset avodv:1668097141214-5
ALTER TABLE "public"."department" ADD CONSTRAINT "fkd25eiwpa8towqc18sr7hbsnn" FOREIGN KEY ("manager_id") REFERENCES "public"."employees" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset avodv:1668097141214-6
ALTER TABLE "public"."department" ADD CONSTRAINT "fke256xyrpq2i7id8gpdgpc8cbf" FOREIGN KEY ("updated_by") REFERENCES "public"."employees" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset avodv:1668097141214-7
ALTER TABLE "public"."employees" ADD CONSTRAINT "fkialhyfka8wy8ljopxkstd1xcr" FOREIGN KEY ("updated_by") REFERENCES "public"."employees" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset avodv:1668097141214-8
ALTER TABLE "public"."employees" ADD CONSTRAINT "fkolojvdrleop0de8xe9vpoplyv" FOREIGN KEY ("dep_id") REFERENCES "public"."department" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

