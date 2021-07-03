# API SPEC

## API Endpoint v1

### Insert/Update Image Firestore
Request:
- Method: POST
- Endpoint: `/api/v1/fireStore/add`
- Body:

```json
{
  "email": "email autentikasi user firebase",
  "imageBase62": "String base64 gambar",
  "seq": "sequence gambar 1,2,3...9"  
}
```

Response:

```json
{
  "code": "200 atau 504",
  "status": "Success or Bad Request"
}
```

### Delete Image Firestore
Request: 
- Method: POST
- Endpoint: `/api/v1/fireStore/remove`
- Body:

```json
{
  "email": "Email user tes@tes.com di firebase",
  "seq": "Sequence gambar yang dihapus"
}
```

Response:

```json
{
  "code": "200 atau 504",
  "status": "Success or Bad Request"
}
```

### Get Image Firestore
Request:
- Method: POST
- Endpoint: `/api/v1/fireStore/getImage/{email}/{seq}`

Response:
- Content-Type: image/png
- code: 200
- status: Success

### Get List Image Firestore
Request:
- Method: POST
- Endpoint: `/api/v1/fireStore`
- Body:

```json
{
  "email": "Email user tes@tes.com di firebase"
}
```

Response:

```json
{
  "code": "200 atau 504",
  "status": "Success or Bad Request",
  "data": [
    {
      "seq": "seq gambar 1",
      "imageBase64": "String base64 gambar"
    },
    {
      "seq": "seq gambar 2",
      "imageBase64": "String base64 gambar"
    }
  ]
}
```

### Save/Update Account User
Request:
- Method: POST
- Endpoint: `/api/v1/userAccount/update`
- Body:

```json
{
  "about": "Email user tes@tes.com di firebase",
  "passion": "Passion data",
  "jobTitle": "Job title here",
  "company": "company name",
  "school": "nama sekolah",
  "city": "nama kota",
  "gender": "Male/Female"
}
```

Response:

```json
{
  "code": "200 atau 504",
  "status": "Success or Bad Request"
}
```
