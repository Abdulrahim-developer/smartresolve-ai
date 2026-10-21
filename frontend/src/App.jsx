import React, { useState } from 'react';
import { useAuth } from './AuthContext';
import StudentDashboard from './StudentDashboard';
import AdminDashboard from './AdminDashboard';
import Register from './Register'; 
import { ShieldCheck, Mail, Lock, AlertCircle, Loader2, CheckCircle2 } from 'lucide-react';

function App() {
  // Global session states extracted from your secure AuthContext memory core
  const { login, isAuthenticated, user } = useAuth();

  // Screen View Control States: 'LOGIN' or 'REGISTER'
  const [currentView, setCurrentView] = useState('LOGIN');

  // Input Field Form States
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  
  // Dynamic Alert Feedback Notification States
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');
  const [successMessage, setSuccessMessage] = useState('');

  /**
   * Intercepts registration hooks to trigger automated email pre-filled redirections
   */
  const handleRegistrationSuccess = (registeredEmail) => {
    setEmail(registeredEmail); // Pre-fills the email address field instantly
    setPassword(''); // Clears out older stale password parameters
    setErrorMessage('');
    setSuccessMessage('Account registered successfully! Sign in below using your credentials.');
    setCurrentView('LOGIN'); // Drops the screen view layout cleanly back to your login gate
  };

  const handleLoginSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setErrorMessage('');
    setSuccessMessage('');

    try {
      // Dispatches data straight across port 8080 down to your backend controllers via Axios
      await login(email, password);
    }
   catch (error) {
      // Intercept technical status codes and display user-friendly messages
      if (error?.response?.status === 403 || error?.response?.status === 401) {
        setErrorMessage('Invalid email or password. Please double-check your credentials.');
      } else if (error?.response?.status === 404) {
        setErrorMessage('Account does not exist. Please register first.');
      } else {
        // Fallback for general server dropouts or internet disconnection states
        setErrorMessage('Unable to connect to the campus server. Please try again later.');
      }
    }
    
    finally {
      setLoading(false);
    }
  };

  // ==================== SECURITY FIREWALL STATE ROUTER GATES ====================
  if (isAuthenticated) {
    if (user?.role === 'REPORTER') {
      return <StudentDashboard />;
    }
    if (user?.role === 'ADMIN' || user?.role === 'STAFF') {
      return <AdminDashboard />;
    }
  }

  // ==================== CONDITIONAL WORKSPACE NAVIGATION VIEWS ====================
  if (currentView === 'REGISTER') {
    return (
      <Register 
        onRegisterSuccess={handleRegistrationSuccess}
        onBackToLogin={() => {
          setErrorMessage('');
          setSuccessMessage('');
          setCurrentView('LOGIN');
        }}
      />
    );
  }

      // DEFAULT VIEWPORT OUTCOME: Render the premium modern split-screen university login canvas
  return (
    <div className="min-h-screen bg-slate-50 flex flex-col md:flex-row antialiased selection:bg-blue-500 selection:text-white animate-fade-in">
      
      {/* LEFT PANEL: PREMIUM MODERN GRADIENT SIDEBAR WITH AMBIENT LIGHTING MESH (38% WIDTH) */}
      <div className="relative md:w-[38%] bg-slate-950 text-white p-8 md:p-16 flex flex-col justify-between overflow-hidden">
        <div className="absolute top-[-20%] left-[-20%] w-[100%] h-[100%] bg-radial from-blue-600/30 via-transparent to-transparent opacity-70 pointer-events-none" />
        <div className="absolute bottom-[-20%] right-[-10%] w-[90%] h-[90%] bg-radial from-purple-600/20 via-transparent to-transparent opacity-60 pointer-events-none" />
        
        <div className="relative z-10 flex items-center gap-2.5">
          <div className="p-1.5 bg-white/10 backdrop-blur-md rounded-lg border border-white/15 shadow-inner">
            <ShieldCheck className="h-5 w-5 text-blue-400" />
          </div>
          <span className="text-lg font-bold tracking-tight text-white/95">SmartResolve AI</span>
        </div>
        
        <div className="relative z-10 my-16 md:my-0 max-w-sm">
          <h2 className="text-3xl font-extrabold tracking-tight mb-5 leading-[1.2] text-white">
            The institutional workspace where student issues find immediate resolution paths.
          </h2>
          <p className="text-slate-400 font-medium text-sm leading-relaxed">
            Expose and route university grievances instantly using standard multi-threaded Multinomial Naive Bayes text classification algorithms.
          </p>
        </div>

        <div className="relative z-10 flex flex-col gap-4">
          <div className="pt-4 border-t border-white/10 text-xs font-medium tracking-wide text-slate-500 leading-normal">
            "Automated triage processes text inputs instantly, reducing manual support desk delay fields."
            <div className="mt-2 text-slate-400 font-semibold flex items-center gap-2">
              <span className="inline-block h-1.5 w-1.5 rounded-full bg-blue-500" />
              SmartResolve Operational Hub • v1.0.0
            </div>
          </div>
        </div>
      </div>

      {/* RIGHT PANEL: MINIMALIST ACCESSIBLE WORK CANVAS FOR MULTI-ROLE INGESTION (62% WIDTH) */}
      <div className="flex-1 flex items-center justify-center p-8 md:p-24 bg-white md:bg-slate-50">
        <div className="max-w-[420px] w-full bg-white md:p-10 md:rounded-2xl md:shadow-xl md:border md:border-slate-200/60 transition-all duration-300">
          <div className="mb-8">
            <h1 className="text-2xl font-bold text-slate-900 tracking-tight mb-2">
              Welcome back
            </h1>
            <p className="text-slate-500 font-medium text-sm">
              Sign in to manage secure stateless session credentials.
            </p>
          </div>

          {/* DYNAMIC ERROR STATUS CONTAINER ACCENT BLOCK */}
          {errorMessage && (
            <div className="mb-6 p-4 bg-amber-50/80 border border-amber-200/70 rounded-xl flex items-start gap-3">
              <AlertCircle className="h-4.5 w-4.5 text-amber-600 flex-shrink-0 mt-0.5" />
              <span className="text-xs font-semibold text-amber-800 leading-normal">
                {errorMessage}
              </span>
            </div>
          )}

          {/* DYNAMIC REGISTRATION SUCCESS FLASH HIGHLIGHTER NOTE */}
          {successMessage && (
            <div className="mb-6 p-4 bg-emerald-50 border border-emerald-200 rounded-xl flex items-start gap-3 animate-fade-in">
              <CheckCircle2 className="h-4.5 w-4.5 text-emerald-600 flex-shrink-0 mt-0.5" />
              <span className="text-xs font-semibold text-emerald-800 leading-normal">
                {successMessage}
              </span>
            </div>
          )}

          <form onSubmit={handleLoginSubmit} className="space-y-5">
            {/* UNIVERSITY EMAIL IDENTITY INPUT BLOCK */}
            <div>
              <label className="block text-xs font-bold tracking-wide text-slate-700 mb-2">
                University Email Address
              </label>
              <div className="relative group">
                <div className="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none">
                  <Mail className="h-4 w-4 text-slate-400 group-focus-within:text-blue-500 transition-colors" />
                </div>
                <input
                  type="email"
                  required
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  placeholder="e.g., student@university.edu"
                  className="block w-full pl-10 pr-4 py-2.5 bg-white border border-slate-200 rounded-xl font-medium placeholder-slate-400/80 text-slate-900 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 transition-all duration-200"
                />
              </div>
            </div>

            {/* PASSWORD ELEMENT INPUT BLOCK */}
            <div>
              <label className="block text-xs font-bold tracking-wide text-slate-700 mb-2">
                Password
              </label>
              <div className="relative group">
                <div className="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none">
                  <Lock className="h-4 w-4 text-slate-400 group-focus-within:text-blue-500 transition-colors" />
                </div>
                <input
                  type="password"
                  required
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  placeholder="••••••••"
                  className="block w-full pl-10 pr-4 py-2.5 bg-white border border-slate-200 rounded-xl font-medium placeholder-slate-400/80 text-slate-900 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 transition-all duration-200"
                />
              </div>
            </div>

            {/* CTA ACTION UTILITY BUTTON SUBMITTER WITH LIGHTWEIGHT VECTOR LOADER */}
            <button
              type="submit"
              disabled={loading}
              className="w-full py-2.5 bg-blue-600 text-white font-semibold rounded-xl shadow-sm hover:bg-blue-700 transition-all active:scale-[0.985] disabled:opacity-50 disabled:pointer-events-none flex items-center justify-center gap-2 text-sm mt-3 font-medium cursor-pointer"
            >
              {loading ? (
                <>
                  <Loader2 className="h-4 w-4 animate-spin text-white" />
                  <span>Verifying Credentials...</span>
                </>
              ) : (
                <span>Sign in</span>
              )}
            </button>
          </form>

          <div className="mt-6 text-center">
            <span className="text-xs font-medium text-slate-500">
              New to the portal?{' '}
              <button 
                onClick={() => {
                  setErrorMessage('');
                  setSuccessMessage('');
                  setCurrentView('REGISTER');
                }}
                className="text-blue-600 font-semibold hover:underline bg-transparent border-none p-0 cursor-pointer"
              >
                Create an account
              </button>
            </span>
          </div>
        </div>
      </div>

    </div>
  );
}

export default App;
