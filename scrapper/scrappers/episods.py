from bs4 import BeautifulSoup
import requests_cache
import uuid 
# make cached request to the website
BASE_URL = "https://vidcloud9.com/videos/"

def get_episods(slug):
    session = requests_cache.CachedSession('api_session')
    content = None 
    for i in range(30):
        request = session.get(BASE_URL + slug)
        content = request.content 
    # soup 
    soup = BeautifulSoup(content, "html.parser")
    # get movie_frame
    episod_image = soup.select("#main_bg > div:nth-child(5) > div > div.video-info-left > ul > li:nth-child(n) > a > div.img > div.picture > img")
    video_link = soup.select("#main_bg > div:nth-child(5) > div > div.video-info-left > ul > li:nth-child(n) > a")
    episod_title = soup.select("#main_bg > div:nth-child(5) > div > div.video-info-left > ul > li:nth-child(n) > a > div.name")
    episod_meta = soup.select("#main_bg > div:nth-child(5) > div > div.video-info-left > ul > li:nth-child(n) > a > div.meta > span")

    # Get recommended data
    episods = []
    for title, image, video, meta in zip(
        episod_title,
        episod_image,
        video_link,
        episod_meta):

        episods.append(
            {
                "id": str(uuid.uuid4()),
                "title": title.decode_contents().strip(),
                "image": image['src'],
                "video": video['href'],
                "uploaded_time": meta.decode_contents().strip(),
            }
        )
    return episods



if __name__ == "__main__":
    print(get_episods("90-day-fiance-happily-ever-after-season-6-episode-10"))