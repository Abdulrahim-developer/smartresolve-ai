import React, { useState } from 'react';
import api from './services/api'; // ──► Imported our real Axios client network proxy wrapper
import { ShieldCheck, Mail, Lock, User, AlertCircle, Loader2 } from 'lucide-react';

function Register({ onRegisterSuccess, onBackToLogin }) {
  // Local state managers to control interactive sign-up fields
  const [displayName, setDisplayName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [role, setRole] = useState('REPORTER'); // Type-safe user boundary role selector
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');

  const handleRegisterSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setErrorMessage('');

    try {
      // DISPATCH THE REAL DATA TO YOUR INTELLIJ ENDPOINT PATHWAY!
      const response = await api.post('/auth/register', { 
        displayName, 
        email, 
        password, 
        role 
      });
      
      // If your backend responds with a success payload, fire the success callback
      onRegisterSuccess(email);
    } catch (error) {
      // Intercept execution drops and extract clean string error indicators
      setErrorMessage(error.message || String(error) || 'Registration sequence encountered an error.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-slate-50 flex flex-col md:flex-row antialiased selection:bg-blue-500 selection:text-white">
      
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
            Create an institutional identity to file and triage university grievances.
          </h2>
          <p className="text-slate-400 font-medium text-sm leading-relaxed">
            Gain immediate access to automated ticket routing pipelines running on multi-threaded Java 21 container networks.
          </p>
        </div>

        <div className="relative z-10 flex flex-col gap-4">
          <div className="pt-4 border-t border-white/10 text-xs font-medium tracking-wide text-slate-500 leading-normal">
            SmartResolve Operational Hub • v1.0.0
          </div>
        </div>
      </div>

      {/* RIGHT PANEL: ACCESSIBLE WORK CANVAS FOR ACCOUNT SCAFFOLDING (62% WIDTH) */}
      <div className="flex-1 flex items-center justify-center p-8 md:p-24 bg-white md:bg-slate-50">
        <div className="max-w-[420px] w-full bg-white md:p-10 md:rounded-2xl md:shadow-xl md:border md:border-slate-200/60 transition-all duration-300">
          <div className="mb-8">
            <h1 className="text-2xl font-bold text-slate-900 tracking-tight mb-2">
              Create an account
            </h1>
            <p className="text-slate-500 font-medium text-sm">
              Sign up to access secure stateless session routing.
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

          <form onSubmit={handleRegisterSubmit} className="space-y-4">
            {/* FULL NAME ELEMENT INPUT BLOCK */}
            <div>
              <label className="block text-xs font-bold tracking-wide text-slate-700 mb-1.5">
                Full name
              </label>
              <div className="relative group">
                <div className="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none">
                  <User className="h-4 w-4 text-slate-400 group-focus-within:text-blue-500 transition-colors" />
                </div>
                <input
                  type="text"
                  required
                  value={displayName}
                  onChange={(e) => setDisplayName(e.target.value)}
                  placeholder="e.g., Abdul Rahim"
                  className="block w-full pl-10 pr-4 py-2.5 bg-white border border-slate-200 rounded-xl font-medium placeholder-slate-400/80 text-slate-900 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 transition-all duration-200"
                />
              </div>
            </div>

            {/* UNIVERSITY EMAIL IDENTITY INPUT BLOCK */}
            <div>
              <label className="block text-xs font-bold tracking-wide text-slate-700 mb-1.5">
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
                  placeholder="e.g., abdul@university.edu"
                  className="block w-full pl-10 pr-4 py-2.5 bg-white border border-slate-200 rounded-xl font-medium placeholder-slate-400/80 text-slate-900 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 transition-all duration-200"
                />
              </div>
            </div>

            {/* INSTITUTIONAL ROLE PROFILE ACCESS SELECTION BLOCK */}
            <div>
              <label className="block text-xs font-bold tracking-wide text-slate-700 mb-1.5">
                Portal System Role
              </label>
              <select
                value={role}
                onChange={(e) => setRole(e.target.value)}
                className="block w-full px-3.5 py-2.5 bg-white border border-slate-200 rounded-xl font-semibold text-slate-700 focus:outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 text-sm cursor-pointer transition-all duration-200"
              >
                <option value="REPORTER">STUDENT PROFILE</option>
                <option value="STAFF">FACULTY STAFF / OPERATOR</option>
                <option value="ADMIN">SYSTEM ADMINISTRATOR CORE</option>
              </select>
            </div>

            {/* PASSWORD ELEMENT INPUT BLOCK */}
            <div>
              <label className="block text-xs font-bold tracking-wide text-slate-700 mb-1.5">
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

            {/* CTA ACTION UTILITY BUTTON SUBMITTER */}
            <button
              type="submit"
              disabled={loading}
              className="w-full py-2.5 bg-blue-600 text-white font-semibold rounded-xl shadow-sm hover:bg-blue-700 transition-all active:scale-[0.985] disabled:opacity-50 disabled:pointer-events-none flex items-center justify-center gap-2 text-sm mt-3 font-medium cursor-pointer"
            >
              {loading ? (
                <>
                  <Loader2 className="h-4 w-4 animate-spin text-white" />
                  <span>Registering Identity...</span>
                </>
              ) : (
                <span>Register Account</span>
              )}
            </button>
          </form>

          <div className="mt-6 text-center">
            <span className="text-xs font-medium text-slate-500">
              Already have an account?{' '}
              <button
                onClick={onBackToLogin}
                className="text-blue-600 font-semibold hover:underline bg-transparent border-none p-0 cursor-pointer"
              >
                Sign in instead
              </button>
            </span>
          </div>
        </div>
      </div>

    </div>
  );
}

export default Register;
