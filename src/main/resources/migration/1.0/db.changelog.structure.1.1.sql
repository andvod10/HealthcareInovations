-- liquibase formatted sql

-- changeset avodv:1668537185950-1
CREATE TABLE "public"."token" ("id" VARCHAR(255) NOT NULL, "created_at" TIMESTAMP WITHOUT TIME ZONE NOT NULL, "updated_at" TIMESTAMP WITHOUT TIME ZONE NOT NULL, "access_token" VARCHAR(1024), "refresh_token" VARCHAR(1024), "created_by" VARCHAR(255), "updated_by" VARCHAR(255), "employee_id" VARCHAR(255), CONSTRAINT "token_pkey" PRIMARY KEY ("id"));

-- changeset avodv:1668537185950-2
ALTER TABLE "public"."employees" ADD "account_role" INTEGER;

-- changeset avodv:1668537185950-3
ALTER TABLE "public"."employees" ADD "is_active" BOOLEAN;

-- changeset avodv:1668537185950-4
ALTER TABLE "public"."token" ADD CONSTRAINT "fkm7vx4l4mwg56vn65s0mg9lskg" FOREIGN KEY ("created_by") REFERENCES "public"."employees" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset avodv:1668537185950-5
ALTER TABLE "public"."token" ADD CONSTRAINT "fkpl3buw6o3i8aofm0os26gsi3c" FOREIGN KEY ("updated_by") REFERENCES "public"."employees" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset avodv:1668537185950-6
ALTER TABLE "public"."token" ADD CONSTRAINT "fkryipflmv40b6pndxmsl7mi717" FOREIGN KEY ("employee_id") REFERENCES "public"."employees" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

