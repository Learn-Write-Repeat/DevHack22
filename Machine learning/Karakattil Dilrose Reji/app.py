from flask import Flask, render_template, request
import pickle
 
app = Flask(__name__)

@app.route('/', methods=['POST','GET'])
def home():
	return render_template('main.html')

@app.route('/check', methods=['POST','GET'])
def check():
    ri = float(request.form['ri'])
    na = float(request.form['na'])
    mg = float(request.form['mg'])
    al = float(request.form['al'])
    si = float(request.form['si'])
    k = float(request.form['k'])
    ca = float(request.form['ca'])
    ba = float(request.form['ba'])
    fe = float(request.form['fe'])
	

    types= {'1':'building_windows_float_processed',
            '2': "building_windows_non_float_processed",
            '3': "vehicle_windows_float_processed",
            '4': "vehicle_windows_non_float_processed",
            '5': "containers",
            '6': "tableware",
            '7': "headlamps"
        }       
    d =[[ri,na,mg,al,si,k,ca,ba,fe]]
    with open('db.model', 'rb') as f:
        model = pickle.load(f)
    res = model.predict(d)
    return render_template('main.html', msg="Predicted Glass Type: " + types[str(res[0])])


if __name__ == '__main__':
	app.run(debug=True)