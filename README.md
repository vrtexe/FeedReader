# FeedReader

Multi-modular Domain Driven Design application using maven and spring boot with react frontend

## Starting the project
---    
    git clone https://github.com/DragonsLacky/FeedReader.git 
    wget -qO- https://dlcdn.apache.org/kafka/3.0.0/kafka_2.13-3.0.0.tgz | tar -xvz 
    cd kafka_2.13-3.0.0/ 
    bin/zookeeper-server-start.sh config/zookeeper.properties 
    bin/kafka-server-start.sh config/server.properties
    
## Example feed Sources
---
    https://www.buzzfeed.com/world.xml
    https://www.polygon.com/rss/index.xml
    https://www.eurogamer.net/?format=rss
    https://itsfoss.com/rss
    https://kotaku.com/rss
    https://www.theguardian.com/world/rss
    https://www.gameinformer.com/feeds/thefeedrss.aspx
    http://pitchfork.com/rss/news/
    https://www.zdnet.com/news/rss.xml
    https://www.udiscovermusic.com/feed
    https://screenrant.com/feed/
    https://www.cnbc.com/id/100727362/device/rss/rss.html
    https://www.pcgamer.com/rss/
    https://consequenceofsound.net/feed/
    https://www.gamespot.com/feeds/mashup
    https://www.animenewsnetwork.com/all/rss.xml?ann-edition=us
    https://www.comingsoon.net/news/rss-main-30.php
    https://www.theverge.com/web/rss/index.xml
    https://lwlies.com/feed/
    https://www.mirror.co.uk/news/?service=rss
    ttps://blogs.windows.com/rss
    https://www.theverge.com/microsoft/rss/index.xml
    https://www.theverge.com/rss/on-the-verge/index.xml
