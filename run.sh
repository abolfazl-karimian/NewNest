git clone https://github.com/abolfazl-karimian/NewNest.git
cd NewNest
mkdir "store-volume"
docker build -t newnest-app  .
docker run --name newnest-app -v ./store-volume:/opt/nest/store -d newnest-app