from bs4 import BeautifulSoup
import requests_cache
import uuid 
# make cached request to the website
BASE_URL = "https://vidcloud9.com/search.html?keyword="

def search_movie(slug="along-for-the-ride"):
    session = requests_cache.CachedSession('api_session')
    content = None 
    for i in range(30):
        request = session.get(BASE_URL + slug)
        content = request.content 
    # soup 
    soup = BeautifulSoup(content, "html.parser")
    # search 
    search_video = soup.select("#main_bg > div:nth-child(5) > div > div.vc_row.wpb_row.vc_row-fluid.vc_custom_1404913114846 > div.vc_col-sm-12.wpb_column.column_container > div > div > ul > li:nth-child(n) > a")
    search_image = soup.select("#main_bg > div:nth-child(5) > div > div.vc_row.wpb_row.vc_row-fluid.vc_custom_1404913114846 > div.vc_col-sm-12.wpb_column.column_container > div > div > ul > li:nth-child(n) > a > div.img > div.picture > img")
    search_title = soup.select("#main_bg > div:nth-child(5) > div > div.vc_row.wpb_row.vc_row-fluid.vc_custom_1404913114846 > div.vc_col-sm-12.wpb_column.column_container > div > div > ul > li:nth-child(n) > a > div.name")
    search_uploaded = soup.select("#main_bg > div:nth-child(5) > div > div.vc_row.wpb_row.vc_row-fluid.vc_custom_1404913114846 > div.vc_col-sm-12.wpb_column.column_container > div > div > ul > li:nth-child(n) > a > div.meta > span")
    
    # Get recommended data
    search_result = []
    for title, image, video, uploaded in zip(
        search_title,
        search_image,
        search_video,
        search_uploaded):

        search_result.append(
            {
                "id": str(uuid.uuid4()),
                "title": title.decode_contents().strip(),
                "image": image['src'],
                "video": video['href'],
                "uploaded_time": uploaded.decode_contents().strip(),
            }
        )
    return search_result
    
if __name__ == "__main__":
    print(search_movie("rush hour"))