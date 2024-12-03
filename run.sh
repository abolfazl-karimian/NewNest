mkdir "store-volume"
docker build -t newnest-app  .
docker run --name newnest-app -v ./store-volume:/opt/nest/store -d newnest-app