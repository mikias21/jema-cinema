from flask import Flask, request, make_response, jsonify

# movie data 
from scrappers.recent_uploaded import get_recent_content
from scrappers.movies import get_movies
from scrappers.series import get_series
from scrappers.cinema import get_cinema
from scrappers.recommeded import get_recommended
from scrappers.single_movie import get_movie_link, get_description
from scrappers.recommendations import get_recommendations
from scrappers.episods import get_episods
from scrappers.search import search_movie

app = Flask(__name__)

@app.route("/api/movies/home", methods=['GET'])
def get_home_content():
    if request.method == 'GET':
        try:
            home_content = {
            'recommended': get_recommended(),
            'recent': get_recent_content(),
            'cinema': get_cinema(),
            'movies': get_movies(),
            'series': get_series()
            }
            resp = make_response(home_content)
            resp.headers.set("Access-Control-Allow-Origin", "*")
            resp.headers.set("Access-Control-Allow-Methods", "GET")
            resp.headers.set("Access-Control-Allow-Headers", "*")
            return resp
        except Exception as e:
            return jsonify({"error": str(e)})

@app.route('/api/movies/movie', methods=['GET'])
def get_movie_link_api():
    if request.method == "GET":
        title = request.args.get("title")
        link = None
        if len(title) != 0:
            try:
                link = get_movie_link(title)
            except Exception as e:
                return jsonify({"error": str(e)})
        movie = {"link": link}
        resp = make_response(movie)
        resp.headers.set("Access-Control-Allow-Origin", "*")
        resp.headers.set("Access-Control-Allow-Methods", "GET")
        resp.headers.set("Access-Control-Allow-Headers", "*")        
        return resp

@app.route("/api/movies/recommendations", methods=['GET'])
def get_recommendations_api():
    recommendation = {"recommendations": get_recommendations()}
    resp = make_response(recommendation)
    resp.headers.set("Access-Control-Allow-Origin", "*")
    resp.headers.set("Access-Control-Allow-Methods", "GET")
    resp.headers.set("Access-Control-Allow-Headers", "*")        
    return resp

@app.route("/api/movies/epidesc", methods=['GET'])
def get_episod_description():
    if request.method == 'GET':
        slug = request.args.get("title")
        if len(slug) != 0:
            try:
                epi_desc = {
                    'episode': get_episods(slug),
                    'description': get_description(slug)
                }
            except Exception as e:
                return jsonify({"error": str(e)})
    resp = make_response(epi_desc)
    resp.headers.set("Access-Control-Allow-Origin", "*")
    resp.headers.set("Access-Control-Allow-Methods", "GET")
    resp.headers.set("Access-Control-Allow-Headers", "*")        
    return resp

@app.route("/api/movies/search", methods=['GET'])
def search_movie_api():
    if request.method == "GET":
        query = request.args.get("query")
        if len(query) != 0:
            try:
                search_result = {
                    'result': search_movie(query),
                }
            except Exception as e:
                return jsonify({"error": str(e)})
        resp = make_response(search_result)
        resp.headers.set("Access-Control-Allow-Origin", "*")
        resp.headers.set("Access-Control-Allow-Methods", "GET")
        resp.headers.set("Access-Control-Allow-Headers", "*")        
        return resp

if __name__ == "__main__":
    app.run(port=9090)
    app.debug(True)