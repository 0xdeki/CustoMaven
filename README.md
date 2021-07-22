# CustoMaven
Minimal private maven repo server written in Java

## What is it?
CustoMaven is a web server written with the Spring framework. It lets you serve Maven artifacts from your own self-hosted private repository. 

## How do I use it?
CustoMaven runs on port 80 by default. Set it up either locally or remotely, just make sure it's accessible over http on the port you chose. The port can be changed in `application.properties`.

1. Either compile the source code or grab the latest release from [here](https://github.com/0xdeki/CustoMaven/releases/).
2. CustoMaven will set up a folder in `/user/CustoMaven/repositories`. In here, create a folder with any URL-friendly name. In this example we will name it `my-repo`. This will also be the name of your repository. You can create as many repositories as you want.
3. From your local Maven repository (usually located at `/user/.m2/repository`), copy the libraries you want to host on your server. Make sure to preserve the folder structure (using json as an example, the folder could be `/org/json/json/20160810/json-20160810.*`. The full path on the server would be `/user/CustoMaven/repositories/my-repo/org/json/json/20160810/json-20160810.*`). 
4. Add the repository to your Maven project(s). In our case, the URL would be http://hostname/repos/my-repo

Example implementation in a .pom file:
```
...
    <repositories>
        <repository>
            <id>CustoMaven repo</id>
            <url>http://hostname/repos/my-repo</url>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
        </repository>
    </repositories>
...
```

Maven should now be able to pick up your artifacts from your private repository.

## How does this actually work? / How Maven fetches artifacts
When Maven looks for artifacts in a repository, it checks for a file in a certain directory. As it turns out, this directory is `repo-url/{group-id}/{artifact-id}/{version}/{artifact-id}-{version}.jar`. If we use the JSON library as an example with our example repo, the full URL looks like this: http://hostname/repos/my-repo/org/json/json/20160810/json-20160810.jar (the JSON library is in fact [this](https://mvnrepository.com/artifact/org.json/json/20160810) artifact). This means that if we place the libraries where Maven expects them to be it can find them and download them. If you want a more hands-on look at the structure, your local maven repository (at `/user/.m2/`) has the same structure, which is why we copy the libraries from there. This essentially means that we can use an apache- or nginx-server to host a private maven repository, but where's the fun in that? CustoMaven works by setting up the entire `/repositories/`-folder as a static resource folder, which lets us put anything in there and Spring will serve the files if you ask for them. Basically, we route `http://hostname/repos/` to the `/repositories/`-folder.

## Todo
* Make managing repositories and artifacts more user-friendly, preferrably with some sort of user interface. Right now this project is about as useful as a standard web server
