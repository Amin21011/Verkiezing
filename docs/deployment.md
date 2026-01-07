# -- Deployment 

## Front-end (Amin)

## Back-end (Ramzi)

### How we deployed the backend


First go to the backend folder in the terminal and run:

`docker build -t <dockerhub-username>/election-backend .`

This builds the Docker image for the backend.

After you have built the docker image you need to push the image to Docker Hub so OEGE can acces it:

`docker push <dockerhub-username>/election-backend`

When you're done with that proceed to login into OEGE on your pc terminal. After you've logged in pull the image from docker 
hub: `docker pull <dockerhub-username>/election-backend`

Finally, start the backend container on OEGE: 
`docker run -d \
--restart=always \
--name election-backend \
-p 0.0.0.0:9122:8080 \
<dockerhub-username>/election-backend`

the backend will now be running at:
http://oege.ie.hva.nl:9122

(Port 9122 is just an example, any free port between 8000-10000 can be used)

### How did we deploy and why did we choose this method? (Morsal)

### Diagram: (Betul)

