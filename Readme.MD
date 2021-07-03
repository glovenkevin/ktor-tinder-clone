# API SPEC
For your information: Letakan service account json di folder 
resources dengan nama `firebase-key.json`.

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
  "code": "200 atau 503",
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
  "code": "200 atau 503",
  "status": "Success or Bad Request"
}
```

### Get Image Firestore
Request:
- Method: POST
- Endpoint: `/api/v1/fireStore/getImage/{email}/{seq}`

Response:
- Success Response
    - Content-Type: image/png
    - code: 200
    - status: Success
    - body: `ByteArray`
- Error Response

```json
{
  "code": "200 atau 503",
  "status": "Bad Request",
  "data": "Gambar tidak ditemukan"
}
```

### Get List Image Firestore
Request:
- Method: GET
- Endpoint: `/api/v1/fireStore/getListImage/{email}`

Response:

```json
{
  "code": "200 atau 503",
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
  "email": "email user",
  "userName": "Username baru",
  "firstName": "firstName user",
  "about": "Tentang / Biografi / penjelasan singkat si user",
  "passion": "Passion data",
  "jobTitle": "Job title here",
  "company": "company name",
  "school": "nama sekolah",
  "city": "nama kota",
  "gender": "Man/Woman",
  "birthDay": "Tanggal YYYY-MMM-D atau 2000-05-20"
}
```

Response:

```json
{
  "code": "200 atau 503",
  "status": "Success or Bad Request"
}
```

### Get Random Bio
Request:
- Method: POST
- Endpoint: `/api/v1/userAccount/getRandomBio`
- Body:

```json
{
  "age": 23,
  "gender": "Male/Female",
  "userName": "userName milik user"
}
```

Response:

```json
{
  "userName": "Username si user",
  "age": 20,
  "about": "Bio si user"
}
```

### Triger Notification


### Update Like or Skip

