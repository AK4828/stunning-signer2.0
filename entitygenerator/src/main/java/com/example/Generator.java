package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class Generator {

    public static void main(String... args) throws Exception {
        Schema schema = new Schema(1, "com.meruvian.ldsigner.entity");
        schema.enableKeepSectionsByDefault();
        Entity fileInfo = addFileInfo(schema);
        Entity document = addDocument(schema, fileInfo);
        Entity signedDocument = addSignedDocument(schema, document);

        DaoGenerator daoGenerator = new DaoGenerator();
        daoGenerator.generateAll(schema, "app/src/main/java");
    }

    private static Entity addDefaultPersistence(Entity entity) {
        entity.implementsSerializable();
        entity.implementsInterface("LogInformationAware");
        entity.addLongProperty("dbId").columnName("_id").primaryKey().autoincrement();
        entity.addDateProperty("dbCreateDate").columnName("CREATE_DATE");
        entity.addDateProperty("dbUpdateDate").columnName("UPDATE_DATE");
        entity.addStringProperty("dbCreateBy").columnName("CREATE_BY");
        entity.addStringProperty("dbUpdateBy").columnName("UPDATE_BY");
        entity.addIntProperty("dbActiveFlag").columnName("ACTIVE_FLAG");
        entity.addStringProperty("id").columnName("REF_ID");
        entity.addDateProperty("refCreateDate");
        entity.addStringProperty("refCreateBy");

        return entity;
    }

    private static Entity addDocument(Schema schema, Entity fileInfo) {
        Entity entity = addDefaultPersistence(schema.addEntity("Document"));
        entity.addToOne(fileInfo, entity.addLongProperty("fileInfoId").getProperty());
        entity.addStringProperty("subject");
        entity.addStringProperty("description");
        entity.addStringProperty("content");
        entity.addStringProperty("properties");
        entity.addStringProperty("contentType");
        entity.addStringProperty("status");
        entity.addStringProperty("sha256Hash");

        return entity;
    }

    private static Entity addFileInfo(Schema schema) {
        Entity entity = addDefaultPersistence(schema.addEntity("FileInfo"));
        entity.addStringProperty("originalName");
        entity.addStringProperty("contentType");
        entity.addStringProperty("path");
        entity.addLongProperty("size");

        return entity;
    }

    private static Entity addSignedDocument(Schema schema, Entity document) {
        Entity entity = addDefaultPersistence(schema.addEntity("SignedDocument"));
        entity.addToOne(document, entity.addLongProperty("documentId").getProperty());
        entity.addToOne(entity, entity.addLongProperty("parentDocument").getProperty());
        entity.addByteArrayProperty("signatureBlob");
        entity.addByteArrayProperty("signedDocumentBlob");
        entity.addStringProperty("signatureProperties");
        entity.addStringProperty("signatureType");

        return entity;
    }
}
