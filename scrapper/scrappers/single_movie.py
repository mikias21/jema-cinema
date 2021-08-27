from bs4 import BeautifulSoup
import requests_cache
import uuid 
# make cached request to the website
BASE_URL = "https://vidcloud9.com/videos/"

def get_movie_link(slug):
    session = requests_cache.CachedSession('api_session')
    content = None 
    for i in range(30):
        request = session.get(BASE_URL + slug)
        content = request.content 
    # soup 
    soup = BeautifulSoup(content, "html.parser")
    # get movie_frame
    movie_frame = soup.select("#main_bg > div:nth-child(5) > div > div.video-info-left > div.watch_play > div.play-video > iframe")
    return movie_frame[0]['src']


def get_description(slug):
    session = requests_cache.CachedSession('api_session')
    content = None 
    for i in range(30):
        request = session.get(BASE_URL + slug)
        content = request.content 
    # soup 
    soup = BeautifulSoup(content, "html.parser")
    # get movie_frame
    description = soup.select("#rmjs-1")
    return description[0].decode_contents().strip()


if __name__ == "__main__":
    print(get_description("along-for-the-ride"))