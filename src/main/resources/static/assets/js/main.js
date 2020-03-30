var App = Ember.Application.create();

App.Router.map(function() {
  // index route is created by default
});

App.IndexController = Em.Controller.extend({
    actions: {
        doSearch: function() {
            var self = this;
            self.set('results', null);
            Em.$.get('/patients/search?query='+this.get('searchText')).then(function(data) {
                for (var i=0;i<data.length;i++) {
                    var name = data[i].PatientName;
                    var title = data[i].DocumentTitle;
                    var doc = data[i].DocumentDetails;
                    data[i].name = name;
                    data[i].title = title;
                    data[i].document = doc;
                }
                self.set('results', data);
            });
        } 
    }
});
