from bs4 import BeautifulSoup
import requests_cache
import uuid 
# make cached request to the website
BASE_URL = "https://vidcloud9.com/videos/"

def get_recommendations(slug="along-for-the-ride"):
    session = requests_cache.CachedSession('api_session')
    content = None 
    for i in range(30):
        request = session.get(BASE_URL + slug)
        content = request.content 
    # soup 
    soup = BeautifulSoup(content, "html.parser")
    # recommendations
    recommendation_video = soup.select("#main_bg > div:nth-child(5) > div > div.video-info-right > ul > li:nth-child(n) > a")
    recommendation_image = soup.select("#main_bg > div:nth-child(5) > div > div.video-info-right > ul > li:nth-child(n) > a > div.img > div.picture > img")
    recommendation_title = soup.select("#main_bg > div:nth-child(5) > div > div.video-info-right > ul > li:nth-child(n) > a > div.name")
    recommendation_uploaded = soup.select("#main_bg > div:nth-child(5) > div > div.video-info-right > ul > li:nth-child(n) > a > div.meta > span")
    
    # Get recommended data
    recommendations = []
    for title, image, video, uploaded in zip(
        recommendation_title,
        recommendation_image,
        recommendation_video,
        recommendation_uploaded):

        recommendations.append(
            {
                "id": str(uuid.uuid4()),
                "title": title.decode_contents().strip(),
                "image": image['src'],
                "video": video['href'],
                "uploaded_time": uploaded.decode_contents().strip(),
            }
        )
    return recommendations
    
if __name__ == "__main__":
    print(get_recommendations())