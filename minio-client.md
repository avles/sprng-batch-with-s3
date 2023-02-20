# First download the minio client

curl --insecure -O https://dl.min.io/client/mc/release/linux-amd64/mc

# Copy the download client to your docker instance
docker ps   # note down the id of the minio instance
docker cp mc <containerid>:/tmp  
# example
docker cp mc 691b5fe8a93d:/tmp

# exec to the container
docker exec -it 691b5fe8a93d sh

# set permission
cd /tmp
chmod +x mc

# Configure the client

mc alias set <somename> <minio url> <MINIO_ROOT_USER or access key> <MINIO_ROOT_PASSWORD or secret key >

# example
sh-4.4# ./mc alias set myminio http://localhost:9000 admin black400

# list existing buckets

./mc tree myminio

# create a new bucket named fiserv

./mc mb myminio/fiserv




