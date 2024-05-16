package at.ac.fhcampuswien.fhmdb.exception;

public enum ErrorCodes {
    DATABASE_CONNECTION_ERROR(1001),
    DATABASE_TABLE_CREATION_ERROR(1002),
    DATABASE_DAO_CREATION_ERROR(1003),
    DATABASE_QUERY_ERROR(1004),
    DATABASE_DELETE_ERROR(1005),
    DATABASE_QUERY_BY_ID_ERROR(1006),
    DATABASE_INSERT_ERROR(1007),
    API_REQUEST_ERROR(2001),
    API_RESPONSE_ERROR(2002),
    JSON_PROCESSING_ERROR(2003);
    private final int code;

    ErrorCodes(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}