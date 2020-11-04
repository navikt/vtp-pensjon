import Vue from 'vue'
import Vuex from 'vuex'
import scenarioTemplates from './modules/scenarioTemplates.js'

Vue.use(Vuex)

export default new Vuex.Store({
    modules: {
      scenarioTemplates
    },
    state: {
        backendHost: '',
        apiPath: ''
    },
    getters: {
        getBackendHost: state => {
            return state.backendHost;
        },
        getApiUrl: state => {
            return state.backendHost + state.apiPath;
        }
    },
    mutations: {
        setBackendHost: (state, targetHost) => {
            state.backendHost = targetHost;
        },
        setApiPath: (state, apiPath) => {
            state.apiPath = apiPath;
        }

    },
    actions: {
        setBackendHost: (context) => {
            var targetHost = document.location.protocol + '//' + document.location.hostname + ':' + document.location.port;
            context.commit('setBackendHost', targetHost);

        },
        setApiPath: (context, apiPath) => {
            context.commit('setApiPath', apiPath);
            context.dispatch('loadAvailableTemplates');
        }
    }
})
